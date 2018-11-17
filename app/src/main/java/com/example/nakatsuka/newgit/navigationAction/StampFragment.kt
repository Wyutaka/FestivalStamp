package com.example.nakatsuka.newgit.navigationAction

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import com.example.nakatsuka.newgit.mainAction.controller.beacon.BeaconController
import com.example.nakatsuka.newgit.mainAction.lifecycle.ActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.lifecycle.IActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.model.api.ImageData
import com.example.nakatsuka.newgit.mainAction.model.api.ImageResponse
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import kotlinx.android.synthetic.main.fragment_stamp.*
import org.altbeacon.beacon.BeaconConsumer
import retrofit2.Response


/**配列の最大数を7つ*/
class StampFragment : Fragment(), IActivityLifeCycle, BeaconConsumer {

    val RESULT_SUBACTIVITY: Int = 1000
    var a: fragmentListner? = null

    interface fragmentListner {
        fun goActivity(answerNumber: Int, isSend: Boolean, imageUrl: String)
    }

    //private val buttonResult = mutableListOf(0, 0, 0, 0, 0, 0, 0)
    private var imageUrl = mutableListOf("", "", "", "", "", "", "")
    lateinit var texts: Array<TextView?>
    var data = arrayOf<ImageData?>(null, null, null, null, null, null)

    val TAG = this.javaClass.simpleName
    lateinit var uuid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_stamp, container, false)
        //全てのviewをいったん削除
        container!!.removeAllViews()

        texts = arrayOf(
                null,
                view!!.findViewById(R.id.text1),
                view!!.findViewById(R.id.text2),
                view!!.findViewById(R.id.text3),
                view!!.findViewById(R.id.text4),
                view!!.findViewById(R.id.text5),
                view!!.findViewById(R.id.text6)
        )

        lifecycle.addObserver(ActivityLifeCycle(this))
        uuid = arguments!!.getString("UUID", "")
        Log.d("StampFragment", "uuid = ${uuid}")

        val buttons = arrayOf<Button?>(
                null,
                view.findViewById(R.id.imageButton1),
                view.findViewById(R.id.imageButton2),
                view.findViewById(R.id.imageButton3),
                view.findViewById(R.id.imageButton4),
                view.findViewById(R.id.imageButton5),
                view.findViewById(R.id.imageButton6)
        )
        for (i in 1..6)
            buttons[i]!!.setOnClickListener(event(i))
        //}
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")

        //1〜6へのアクセス禁止
        for (i in 1..6)
            if (buttonResult[i] == 2)
                when (i) {
                    1 -> {
                        imageButton1.setBackgroundResource(0)
                        text1.text = ""
                    }
                    2 -> {
                        imageButton2.setBackgroundResource(0)
                        text2.text = ""
                    }
                    3 -> {
                        imageButton3.setBackgroundResource(0)
                        text3.text = ""
                    }
                    4 -> {
                        imageButton4.setBackgroundResource(0)
                        text4.text = ""
                    }
                    5 -> {
                        imageButton5.setBackgroundResource(0)
                        text5.text = ""
                    }
                    6 -> {
                        imageButton6.setBackgroundResource(0)
                        text6.text = ""
                    }
                }
    }


    private fun makeToast(message: String, x: Int, y: Int) {
        val toast: Toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, x, y / 4)
        toast.show()
    }

    //ProgressDialog機能を追加させました
    private fun event(quizNumber: Int): View.OnClickListener = View.OnClickListener {
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
        if (buttonResult[quizNumber] == 0) {
            val mProgressDialog = ProgressDialog.newInstance("ビーコン取得中...")
            mProgressDialog.setTargetFragment(this, 100)
            mProgressDialog.show(activity!!.supportFragmentManager, "dialog")

            val thread = Thread(Runnable {
                try {
                    Thread.sleep(1000)
                    mProgressDialog.move()
                    Thread.sleep(2050)
                } catch (e: Exception) {
                }

                mProgressDialog.dismiss()
            })
            thread.start()

            //別スレッドでプログレスダイアログ出してる間にそそくさとビーコン取得
            mBeaconController.rangeBeacon {
                if (mProgressDialog.brk)
                    mApiController.requestImage(uuid, quizNumber, it as MutableList<MyBeaconData>, requestImagesFunc(quizNumber))
            }
        }

        if (buttonResult[quizNumber] == 1)
            a!!.goActivity(data[quizNumber - 1]!!.quizCode, data[quizNumber - 1]!!.isSend, data[quizNumber - 1]!!.imageUrl)
    }


    /*Beacon通信用のいろいろ*/
    //Controller関係
    val mApiController = ApiController()
    lateinit var mBeaconController: BeaconController//関数型オブジェクトを返す高階関数(?)です。受け取ったquizCodeの値に応じて、関数型オブジェクトを返します。


    //レスポンスを見つつ最終的な遷移を決定する関数型オブジェクト
    val requestImagesFunc = fun(quizCode: Int): (Response<ImageResponse>) -> Unit {
        val go = fun(response: Response<ImageResponse>) {
            val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
            when (response.code()) {
                200 -> {
                    if (response.body()!!.isSend) {
                        response.body()?.let {
                            data[quizCode - 1] = ImageData(it.quizCode, it.isSend, it.imageURL)
                            imageUrl[quizCode] = it.imageURL
                            val isSend = it.isSend
                            Log.d("judgeAnswer", "${response.body()}")
                            //makeToast("検知範囲内です。", 0, activity!!.for_scale.height)
                            buttonResult[quizCode] = 1
                            texts[quizCode]!!.text = "問題取得済み"
                            Log.d(TAG, "isSend == false")
                            AlertDialog.Builder(activity)
                                    .setTitle("判定結果")
                                    .setMessage(R.string.dialog_in_area)
                                    .setPositiveButton("問題へ") { _, _ ->
                                        a!!.goActivity(quizCode, isSend, imageUrl[quizCode])
                                        //goActivity(quizCode,isSend,"http://cough.cocolog-nifty.com/photos/uncategorized/2017/02/01/gabrieldropout04.jpg")
                                    }
                                    .setNegativeButton("戻る") { _, _
                                        ->
                                        //Do Nothing
                                    }.show()

                        }
                    } else {
                        Log.d(TAG, "isSend == false")
                        AlertDialog.Builder(activity)
                                .setTitle(R.string.dialog_out_of_area)
                                .setMessage("もう一度試してみて下さい！")
                                .setPositiveButton("OK") { _, _
                                    ->
                                    //Do Nothing
                                }.show()

                        /*
                        val builder = AlertDialog.Builder(activity)
                                .setTitle("問題取得失敗")
                                .setMessage("ヒントは地図に！")
                                .setPositiveButton("OK") { _, _ ->
                                }
                        val dialog = builder.show()
                        val textView = dialog.findViewById<TextView>(android.R.id.message)
                        textView.textSize = 26f
                        */
                    }
                }
                else -> {
                    Log.e("judgeAnswer", "${response.code()}, ${response.body()}")
                    AlertDialog.Builder(activity)
                            .setTitle("通信エラー")
                            .setMessage(R.string.dialog_connection_error     )
                            .setPositiveButton("OK") { _, _
                                ->
                                //Do Nothing
                            }.show()
                    //makeToast("HTTPレスポンス != 200。", 0, activity!!.for_scale.height)
                }
            }
        }
        return go
    }

    /*IActivityLifecycle*/
    override fun onCreated() {
        mBeaconController = BeaconController(activity as Context)
        mBeaconController.onCreated()
    }

    override fun onConnected() {
        mBeaconController.bind(activity as BeaconConsumer)
    }

    override fun onDisconnect() {
        mBeaconController.unbind(activity as BeaconConsumer)
    }

    override fun getApplicationContext(): Context = activity!!.applicationContext
    override fun unbindService(p0: ServiceConnection?) = activity!!.unbindService(p0)
    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean = activity!!.bindService(p0, p1, p2)

    override fun onBeaconServiceConnect() {
        mBeaconController.onBeaconServiceConnect()
    }
}


