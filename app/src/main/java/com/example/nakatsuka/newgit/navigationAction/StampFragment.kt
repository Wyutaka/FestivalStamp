package com.example.nakatsuka.newgit.navigationAction

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
import android.widget.Toast
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.APITest
import com.example.nakatsuka.newgit.mainAction.SecondActivity
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import com.example.nakatsuka.newgit.mainAction.controller.beacon.BeaconController
import com.example.nakatsuka.newgit.mainAction.lifecycle.ActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.lifecycle.IActivityLifeCycle
import com.example.nakatsuka.newgit.mainAction.model.api.ImageResponse
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stamp.*
import org.altbeacon.beacon.BeaconConsumer
import retrofit2.Response


class StampFragment : Fragment(), IActivityLifeCycle, BeaconConsumer {

    val RESULT_SUBACTIVITY: Int = 1000
    private val buttonResult = mutableListOf(0, 0, 0, 0, 0, 0, 0)

    val TAG = this.javaClass.simpleName
    lateinit var uuid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_stamp, container, false)

        lifecycle.addObserver(ActivityLifeCycle(this))
        uuid = arguments!!.getString("UUID", "")
        Log.d("StampFragment", "uuid = ${uuid}")

        val buttons = arrayOf<Button>(view.findViewById(R.id.imageButton1), view.findViewById(R.id.imageButton2), view.findViewById(R.id.imageButton3), view.findViewById(R.id.imageButton4), view.findViewById(R.id.imageButton5), view.findViewById(R.id.imageButton6))
        for (i in 1..6)
            buttons[i - 1].setOnClickListener(event(i))

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..6)
            if (buttonResult[i] == 0)
                when (i) {
                    1 -> imageButton1.setBackgroundResource(0)
                    2 -> imageButton2.setBackgroundResource(0)
                    3 -> imageButton3.setBackgroundResource(0)
                    4 -> imageButton4.setBackgroundResource(0)
                    5 -> imageButton5.setBackgroundResource(0)
                    6 -> imageButton6.setBackgroundResource(0)
                }
    }

    private fun goActivity(answerNumber: Int) {
        val isAPI: Boolean
        //TODO:APITestは実APIへ移行
        val APITest = APITest()
        isAPI = APITest.getIsAPI()
        val intent = Intent(activity, SecondActivity::class.java)
        if (buttonResult[answerNumber] == 0) {
            val completed = "すでにスタンプは押されています"
            makeToast(completed, 0, activity!!.for_scale.height)
        } else {
            intent.putExtra("AnswerNumber", answerNumber)
            if (isAPI) {
                startActivityForResult(intent, RESULT_SUBACTIVITY)
            }
        }
    }


    private fun makeToast(message: String, x: Int, y: Int) {
        val toast: Toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, x, y / 4)
        toast.show()
    }

    fun setState(state: Boolean, num: Int) {
        buttonResult[num] = num - 1
    }


    //ProgressDialog機能を追加させました
    private fun event(quizNumber: Int): View.OnClickListener = View.OnClickListener {
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
        if (buttonResult[quizNumber] == 0) {
            mBeaconController.rangeBeacon {
                if (mProgressDialog.brk)
                    mApiController.requestImage(uuid, quizNumber, it as MutableList<MyBeaconData>, requestImagesFunc(quizNumber))
            }
        }
    }


    /*Beacon通信用のいろいろ*/
    //Controller関係
    val mApiController = ApiController()
    lateinit var mBeaconController: BeaconController//関数型オブジェクトを返す高階関数(?)です。受け取ったquizCodeの値に応じて、関数型オブジェクトを返します。


    //レスポンスを見つつ最終的な遷移を決定する関数型オブジェクト
    val requestImagesFunc = fun(quizCode: Int): (Response<ImageResponse>) -> Unit {
        val go = fun(response: Response<ImageResponse>) {
            when (response.code()) {
                200 -> {
                    if (response.body()!!.isSend) {
                        response.body()?.let {
                            //val imageUrl = it.imageURL
                            //val isSend = it.isSend
                            Log.d("judgeAnswer", "${response.body()}")
                            makeToast("検知範囲内です。", 0, activity!!.for_scale.height)
                            //buttonResult[quizCode] = true
                            goActivity(quizCode - 1)
                        }
                    } else {
                        Log.d(TAG, "isSend == false")
                        makeToast("検知範囲外です。", 0, activity!!.for_scale.height)
                    }
                }
                else -> {
                    Log.e("judgeAnswer", "${response.code()}, ${response.body()}")
                    makeToast("HTTPレスポンス != 200。", 0, activity!!.for_scale.height)
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


