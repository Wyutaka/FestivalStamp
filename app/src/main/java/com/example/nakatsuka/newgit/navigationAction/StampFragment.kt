package com.example.nakatsuka.newgit.navigationAction

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.nakatsuka.newgit.mainAction.MainActivity
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import com.example.nakatsuka.newgit.mainAction.controller.beacon.BeaconController
import com.example.nakatsuka.newgit.mainAction.lifecycle.ActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.lifecycle.IActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.model.api.ImageData
import com.example.nakatsuka.newgit.mainAction.model.api.ImageResponse
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stamp.*
import org.altbeacon.beacon.BeaconConsumer
import retrofit2.Response


/**配列の最大数を7つ*/
class StampFragment : Fragment(), IActivityLifeCycle, BeaconConsumer {

    var TAG = this.javaClass.simpleName

    interface fragmentListner {
        fun goActivity(answerNumber: Int, isSend: Boolean,imageURL:String)
        fun take1(answerNumber: Int)
        fun goActivity(answerNumber: Int,isSend: Boolean)
    }

    lateinit var a: fragmentListner

    private val isGot = arrayOf(null,false, false, false, false, false, false)

    //private val buttonResult = mutableListOf(0, 0, 0, 0, 0, 0, 0)
    private var imageUrl = mutableListOf("", "", "", "", "", "", "")
    lateinit var texts: Array<TextView?>
    var data = arrayOf<ImageData?>(null, null, null, null, null, null)

    lateinit var uuid: String
    lateinit var userName: String

    private var mode = false
    private var choice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        a = activity as fragmentListner

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_stamp, container, false)
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")



        texts = arrayOf(
                null,
                view!!.findViewById(R.id.text1),
                view!!.findViewById(R.id.text2),
                view!!.findViewById(R.id.text3),
                view!!.findViewById(R.id.text4),
                view!!.findViewById(R.id.text5),
                view!!.findViewById(R.id.text6)
        )

        for(i in 1..6) {
            if (buttonResult[i] == 1)
                texts[i]!!.text = "\n\n問題取得済み"
            else texts[i]!!.text = "\n\n問題未取得"
        }

        lifecycle.addObserver(ActivityLifeCycle(this))
        uuid = arguments!!.getString("UUID", "")
        userName = arguments!!.getString("USERNAME", "")

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

        if (!choice) {
            AlertDialog.Builder(activity)
                    .setMessage("デバッグモードを使用しますか？")
                    .setPositiveButton("はい") { _, _ ->
                        mode = true
                    }
                    .setNegativeButton("いいえ", null)
                    .show()
            choice = true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
        //a!!.goActivity(1,true)


        for (i in 1..6) {
            if (buttonResult[i] == 2||buttonResult[i] == 3) {

                when (i) {
                    1 -> {
                        imageButton1.setBackgroundResource(0)
                        imageButton1.text = ""
                        text1.text = ""
                    }
                    2 -> {
                        imageButton2.setBackgroundResource(0)
                        imageButton2.text = ""
                        text2.text = ""
                    }
                    3 -> {
                        imageButton3.setBackgroundResource(0)
                        imageButton3.text = ""
                        text3.text = ""
                    }
                    4 -> {
                        imageButton4.setBackgroundResource(0)
                        imageButton4.text = ""
                        text4.text = ""
                    }
                    5 -> {
                        imageButton5.setBackgroundResource(0)
                        imageButton5.text = ""
                        text5.text = ""
                    }
                    6 -> {
                        imageButton6.setBackgroundResource(0)
                        imageButton6.text = ""
                        text6.text = ""
                    }
                }
            }
        }
    }

    //ProgressDialog機能を追加させました
    //ゴール時の反応を追加しました
    private fun event(quizNumber: Int): View.OnClickListener = View.OnClickListener{
        Log.d("quiznumber",quizNumber.toString())
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
        if (buttonResult[quizNumber] == 0 ) {
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

            //TODO delete by koudaisai
            val cheat = mutableListOf<MyBeaconData>()
            if (mode)
                for (i in 1..9)
                    when (quizNumber) {
                        1 ->
                            cheat.add(MyBeaconData(442, 0))
                        2 ->
                            cheat.add(MyBeaconData(298, 0))
                        3 ->
                            cheat.add(MyBeaconData(91, 0))
                        4 ->
                            cheat.add(MyBeaconData(503, 0))
                        5 ->
                            cheat.add(MyBeaconData(844, 0))
                        6 ->
                            cheat.add(MyBeaconData(1212, 0))
                    }

            mBeaconController.rangeBeacon {
                if (mProgressDialog.brk) {
                    if (mode)
                        mApiController.requestImage(activity, uuid, quizNumber, cheat, requestImagesFunc(quizNumber))
                    else
                        mApiController.requestImage(activity, uuid, quizNumber, it as MutableList<MyBeaconData>, requestImagesFunc(quizNumber))
                }
            }
        }

        if (buttonResult[quizNumber] == 1) {
    Log.d("quiznumber",quizNumber.toString())
            a!!.goActivity(quizNumber,true)
        }
        /*if(buttonResult[quizNumber] == 2){
        val completed = "すでにスタンプは押されています"
            AlertDialog.Builder(activity)
                    .setTitle("結果")
                    .setMessage(completed)
                    .setPositiveButton("OK") { _, _ ->

                    }
                    .show()

        }*/

        Log.d("buttonResult",buttonResult[quizNumber].toString())
        if (buttonResult[quizNumber] == 3) {
            val builder = AlertDialog.Builder(activity)
                    .setTitle("ゲームクリア")
                    .setMessage("ゲームを終了しますか？")
                    .setPositiveButton("OK") { _, _ ->

                        val mApiController = ApiController()
                        mApiController.requestGoal(uuid) { response ->
                            when (response.code()) {
                                200 -> {
                                    response.body()?.let {
                                        val builder = AlertDialog.Builder(activity)
                                                .setTitle("クリア済")
                                                .setMessage(userName + "さん、クリアおめでとうございます！景品受取所まで景品(数に限りがございます)を受け取りにお越しください！")
                                                .setPositiveButton("OK") { _, _ ->
                                                }
                                        builder.show()
                                    }
                                }

                                else ->Log.d("goaldesu",response.code().toString())
                            }
                        }

                    }
                    .setNegativeButton("キャンセル") { _, _ ->
                    }
            builder.show()
        }
    }


    /*Beacon通信用のいろいろ*/
    //Controller関係
    val mApiController = ApiController()
    lateinit var mBeaconController: BeaconController//関数型オブジェクトを返す高階関数(?)です。受け取ったquizCodeの値に応じて、関数型オブジェクトを返します。
    //imageRequest時に実行
    val requestImagesFunc = fun(quizCode: Int): (Response<ImageResponse>) -> Unit {
        val go = fun(response: Response<ImageResponse>) {
            val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
            when (response.code()) {
                200 -> {
                    if (response.body()!!.isSend){
                        response.body()?.let {
                            data[quizCode - 1] = ImageData(it.quizCode, it.isSend, it.imageURL)
                            imageUrl[quizCode] = it.imageURL
                            val isSend = it.isSend
                            buttonResult[quizCode] = 1

                            a!!.take1(quizCode)
//                            val pref = PreferenceManager.getDefaultSharedPreferences(activity!!)
//                            val editor = pref.edit()
//                            editor.putInt("buttonResult[answerNumber]", 1)
//                            editor.apply()
//                            //buttonResult[answerNumber!!] = 1

                            texts[quizCode]!!.text = "\n\n問題取得済み"
                            isGot[quizCode] = true

                            Log.d(TAG, "isSend == false")
                            AlertDialog.Builder(activity)
                                    .setTitle("判定結果")
                                    .setMessage(R.string.dialog_in_area)
                                    .setPositiveButton("問題へ") { _, _ ->
                                        a!!.goActivity(quizCode, isSend)

                                    }
                                    .setNegativeButton("戻る") { _, _
                                        ->
                                    }.show()

                        }
                    } else {
                        AlertDialog.Builder(activity)
                                .setTitle(R.string.dialog_out_of_area)
                                .setMessage("もう一度試してみて下さい！")
                                .setPositiveButton("OK") { _, _
                                    ->
                                    //Do Nothing
                                }.show()
                    }
                }
                else -> {
                    AlertDialog.Builder(activity)
                            .setTitle("通信エラー")
                            .setMessage(R.string.dialog_connection_error)
                            .setPositiveButton("OK") { _, _
                                ->
                                //Do Nothing
                            }.show()
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

    /*BeaconConsumer*/
    override fun getApplicationContext(): Context = activity!!.applicationContext
    override fun unbindService(p0: ServiceConnection?) = activity!!.unbindService(p0)
    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean = activity!!.bindService(p0, p1, p2)

    override fun onBeaconServiceConnect() {
        mBeaconController.onBeaconServiceConnect()
    }

}


