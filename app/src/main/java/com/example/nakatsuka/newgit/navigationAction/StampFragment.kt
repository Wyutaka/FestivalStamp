package com.example.nakatsuka.newgit.navigationAction

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.APITest
import com.example.nakatsuka.newgit.mainAction.SecondActivity
import kotlinx.android.synthetic.main.activity_main.*


class StampFragment : Fragment() {

    val RESULT_SUBACTIVITY: Int = 1000
    private val buttonResult = mutableListOf<Boolean>(false, false, false, false, false, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_stamp, container, false)

        //ダイアログが出ます
        val event = View.OnClickListener {
            val mProgressDialog = ProgressDialog.newInstance("ビーコン取得中...")
            mProgressDialog.setTargetFragment(this, 100)
            mProgressDialog.show(activity!!.supportFragmentManager, "dialog")
            val thread = Thread(Runnable {
                try {
                    Thread.sleep(1000)
                    mProgressDialog.move()
                } catch (e: Exception) {
                }
                try {
                    Thread.sleep(2000)
                } catch (e: Exception) {
                }
                mProgressDialog!!.dismiss()
            })
            thread.start()
        }
        view.findViewById<Button>(R.id.imageButton1).setOnClickListener(event)
        view.findViewById<Button>(R.id.imageButton2).setOnClickListener(event)
        view.findViewById<Button>(R.id.imageButton3).setOnClickListener(event)
        view.findViewById<Button>(R.id.imageButton4).setOnClickListener(event)
        view.findViewById<Button>(R.id.imageButton5).setOnClickListener(event)
        view.findViewById<Button>(R.id.imageButton6).setOnClickListener(event)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**

        Log.d("firstdesu","firstdesu")
        imageButton1.setOnClickListener {
        val quizNumber = 0
        goActivity(quizNumber)
        }
        Log.d("firstafter","firstafter")
        imageButton2.setOnClickListener {
        val quizNumber = 1
        goActivity(quizNumber)
        }
        imageButton3.setOnClickListener {
        val quizNumber = 2
        goActivity(quizNumber)
        }
        imageButton4.setOnClickListener {
        val quizNumber = 3
        goActivity(quizNumber)
        }
        imageButton5.setOnClickListener {
        val quizNumber = 4
        goActivity(quizNumber)
        }
        imageButton6.setOnClickListener {
        val quizNumber = 5
        goActivity(quizNumber)
        }

        for(i in 0..5){
        if(buttonResult[i]){
        when(i){
        0 -> imageButton1.setImageResource(0)
        1 -> imageButton2.setImageResource(0)
        2 -> imageButton3.setImageResource(0)
        3 -> imageButton4.setImageResource(0)
        4 -> imageButton5.setImageResource(0)
        5 -> imageButton6.setImageResource(0)
        }
        }
        }
         */
    }


    private fun goActivity(answerNumber: Int) {
        val isAPI: Boolean
        val APITest = APITest()
        isAPI = APITest.getIsAPI()
        val intent = Intent(activity, SecondActivity::class.java)
        if (buttonResult[answerNumber]) {

            val completed = "すでにスタンプは押されています"
            makeToast(completed, 0, for_scale.height)
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
        buttonResult[num] = state
    }

}


