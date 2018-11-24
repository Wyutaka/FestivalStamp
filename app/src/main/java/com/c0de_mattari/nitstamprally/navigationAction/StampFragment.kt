package com.c0de_mattari.nitstamprally.navigationAction

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.c0de_mattari.nitstamprally.R
import com.c0de_mattari.nitstamprally.mainAction.AlertUtil
import com.c0de_mattari.nitstamprally.mainAction.MainActivity
import com.c0de_mattari.nitstamprally.mainAction.controller.api.ApiController
import com.c0de_mattari.nitstamprally.mainAction.controller.beacon.BeaconController
import com.c0de_mattari.nitstamprally.mainAction.model.api.ImageData
import com.c0de_mattari.nitstamprally.mainAction.model.api.ImageResponse
import com.c0de_mattari.nitstamprally.mainAction.model.beacon.MyBeaconData
import kotlinx.android.synthetic.main.fragment_stamp.*
import retrofit2.Response


/**配列の最大数を7つ*/
class StampFragment : Fragment() {


    interface FragmentListner {
        fun goActivity(answerNumber: Int, isSend: Boolean, imageURL: String)
        fun take1(answerNumber: Int)
        fun takeGoal()
        fun saveURL(quizCode: Int, imageURL: String)
        fun isGoal(): Boolean
    }

    lateinit var mFragmentListner: FragmentListner

    private val isGot = arrayOf(null, false, false, false, false, false, false)

    lateinit var texts: Array<TextView?>
    var data = arrayOf<ImageData?>(null, null, null, null, null, null)

    lateinit var uuid: String
    lateinit var userName: String

    private var cheatMode = false
    private var choice = false

    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentListner = activity as FragmentListner

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mBeaconController = BeaconController(activity as Context)
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

        for (i in 1..6) {
            if (buttonResult[i] == 1)
                texts[i]!!.text = "\n\n問題取得済み"
            else texts[i]!!.text = "\n\n問題未取得"
        }

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
            buttons[i]!!.setOnClickListener {
                if (!isProcessing) {
                    event(i)
                }
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")


        for (i in 1..6) {
            if (buttonResult[i] == 2 || buttonResult[i] == 3) {

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
    private fun event(quizNumber: Int) {
        val uuid = arguments!!.getString("UUID")
        val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")

        if (buttonResult[quizNumber] == 0) {
            isProcessing = true
            val mProgressDialog = ProgressDialog.newInstance("ビーコン取得中...")
            mProgressDialog.setTargetFragment(this, 100)

            val thread = Thread(Runnable {
                try {
                    Handler(Looper.getMainLooper()).post {
                        mProgressDialog.show(activity!!.supportFragmentManager, "dialog")
                    }
                    Thread.sleep(1000)
                    isProcessing = mProgressDialog.brk
                    Handler(Looper.getMainLooper()).post {
                        mProgressDialog.move()
                    }
                    Thread.sleep(2050)
                    isProcessing = mProgressDialog.brk
                    Handler(Looper.getMainLooper()).post {
                        mProgressDialog.dismiss()
                    }
                    Thread.sleep(1000)
                    isProcessing = false
                } catch (e: Exception) {
                }
            })
            thread.start()

            (activity as MainActivity).rangeBeacon {
                Log.d("DEBAG", "Rangebeacon called from stampfragment")
                if (mProgressDialog.brk) {
                    mApiController.requestImage(activity, uuid, quizNumber, it as MutableList<MyBeaconData>, requestImagesFunc(quizNumber))
                }
            }
            Log.d("DEBAG", "end")
        }

        if (buttonResult[quizNumber] == 1) {
            Log.d("quiznumber", quizNumber.toString())
            val imageURL = arguments!!.getStringArrayList("imageURL")
            mFragmentListner!!.goActivity(quizNumber, true, imageURL[quizNumber])
        }

        val goalApiIsCalled = mFragmentListner!!.isGoal()
        Log.d("buttonResult", buttonResult[quizNumber].toString())
        if (!goalApiIsCalled) {
            if (buttonResult[quizNumber] == 3) {
                AlertUtil.showYesNoDialog(activity as Activity,"ゲームクリア","ゲームを終了しますか?",true){

                            val mApiController = ApiController()
                            mApiController.requestGoal(uuid) { response ->
                                when (response.code()) {
                                    200 -> {

                                        response.body()?.let {
                                            mFragmentListner!!.takeGoal()
                                            Log.d("", goalApiIsCalled.toString())

                                            AlertUtil.showNotifyDialog(activity as Activity,"クリア済","$userName さん、クリアおめでとうございます！景品受取所(5233教室)まで景品(数に限りがございます)を受け取りにお越しください！")
                                        }
                                    }

                                    else -> Log.d("goaldesu", response.code().toString())
                                }
                            }

                        }
            }
        } else {
            AlertUtil.showNotifyDialog(activity as Activity,"クリア済","$userName さん、クリアおめでとうございます！景品受取所(5233教室)まで景品(数に限りがございます)を受け取りにお越しください！")
        }
    }


    /*Beacon通信用のいろいろ*/
    //Controller関係
    val mApiController = ApiController()
    lateinit var mBeaconController: BeaconController
    //関数型オブジェクトを返す高階関数(?)です。受け取ったquizCodeの値に応じて、関数型オブジェクトを返します。
    //imageRequest時に実行
    val requestImagesFunc = fun(quizCode: Int): (Response<ImageResponse>) -> Unit {
        val go = fun(response: Response<ImageResponse>) {
            Log.d("requestImagesFunc", response.code().toString())
            when (response.code()) {
                200 -> {
                    val buttonResult: IntArray = arguments!!.getIntArray("buttonResult")
                    //TODO:iOSに合わせて文面をを差し替え
                    when(response.body()!!.messageId.toInt())
                    {
                        0 -> {//範囲外
                            AlertUtil.showNotifyDialog(
                                    activity as Activity,
                                    "取得結果",
                                    getString(R.string.dialog_out_of_area)
                            )
                        }
                        1 ->{//範囲付近
                            AlertUtil.showNotifyDialog(
                                    activity as Activity,
                                    "取得結果",
                                    getString(R.string.dialog_near_area)
                            )
                        }
                        2->{
                            //範囲内
                            response.body()?.let {
                                data[quizCode - 1] = ImageData(it.quizCode, it.isSend, it.imageURL)
                                val isSend = it.isSend
                                buttonResult[quizCode] = 1

                                mFragmentListner!!.take1(quizCode)
                                mFragmentListner!!.saveURL(quizCode, it.imageURL)

                                texts[quizCode]!!.text = "\n\n問題取得済み"
                                isGot[quizCode] = true

                                AlertUtil.showYesNoDialog(
                                        activity as Activity,
                                        "判定結果",
                                        getString(R.string.dialog_in_area),
                                        true
                                ) {
                                    mFragmentListner!!.goActivity(quizCode, isSend, it.imageURL)
                                }
                            }
                        }
                        3->{
                            //クリア済
                            //未定義
                        }
                    }
                }
                else -> {
                    AlertUtil.showNotifyDialog(activity as Activity,
                            "通信エラー",
                            "通信エラーが発生しました。"
                    )
                }
            }
        }
        return go
    }

}


