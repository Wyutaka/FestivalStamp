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
import kotlinx.android.synthetic.main.fragment_stamp.*


class StampFragment : Fragment() {

    val RESULT_SUBACTIVITY: Int = 1000
    private val buttonResult = mutableListOf<Boolean>(false, false, false, false, false, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_stamp, container, false)

        //ダイアログ機能を追加させました
        fun event(quizNumber: Int):View.OnClickListener = View.OnClickListener {
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
                    goActivity(quizNumber)
                })
                thread.start()
            }

        view.findViewById<Button>(R.id.imageButton1).setOnClickListener(event(0))
        view.findViewById<Button>(R.id.imageButton2).setOnClickListener(event(1))
        view.findViewById<Button>(R.id.imageButton3).setOnClickListener(event(2))
        view.findViewById<Button>(R.id.imageButton4).setOnClickListener(event(3))
        view.findViewById<Button>(R.id.imageButton5).setOnClickListener(event(4))
        view.findViewById<Button>(R.id.imageButton6).setOnClickListener(event(5))

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 0..5)
            if (buttonResult[i])
                when (i) {
                    0 -> imageButton1.setBackgroundResource(0)
                    1 -> imageButton2.setBackgroundResource(0)
                    2 -> imageButton3.setBackgroundResource(0)
                    3 -> imageButton4.setBackgroundResource(0)
                    4 -> imageButton5.setBackgroundResource(0)
                    5 -> imageButton6.setBackgroundResource(0)
                }

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


