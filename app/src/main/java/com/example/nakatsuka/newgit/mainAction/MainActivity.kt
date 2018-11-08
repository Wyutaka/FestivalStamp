package com.example.nakatsuka.newgit.mainAction

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.nakatsuka.newgit.navigationAction.*
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_main.*

/*Todo 登録後の戻るボタンの制御
  Todo fragmentの処理　
  Todo 不要ボタン*/
class MainActivity : AppCompatActivity() {
    val RESULT_SUBACTIVITY: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("maindesu", "maindesu")


        //itimaie.webViewClient = WebViewClient()
        //itimaie.loadUrl("https://c0de-app.club.nitech.ac.jp/cloud/apps/files/?dir=/Public%20Share%20(C0de)/NitFes2018/www_knoom/design/stamp-picture&fileid=78384#/Public%20Share%20(C0de)/NitFes2018/www_knoom/design/stamp-picture/test.png")

        //fragmentの初期設定
       if(savedInstanceState==null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, StampFragment())
            transaction.commit()

            Log.d("fragmentdesu", "fragmentdesu")
        }



        stamp_rally.setOnClickListener {
            // コードからフラグメントを追加
            Log.d("fragment", "called!!")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //バックスタックを設定
                transaction.addToBackStack(null)
                //パラメータを設定
                transaction.replace(R.id.container, StampFragment())
                transaction.commit()
            }
        }
        map.setOnClickListener {
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, MapFragment())
                //バックスタックを設定
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        notice_board.setOnClickListener {
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, BoardFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        time_table.setOnClickListener {
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, ScheduleFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        others.setOnClickListener {
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, OthersFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            } }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_SUBACTIVITY) {
            if (resultCode == result_canceled) {
            } else if (resultCode == AppCompatActivity.RESULT_OK) {

                val buttonResult = mutableListOf<Boolean>(false, false, false, false, false, false)
                val answerNumber: Int? = intent!!.getIntExtra("answerNumber", 6)
                buttonResult[answerNumber!!] = true

                val mStampFragment = StampFragment()
                when (answerNumber) {
                    0 -> if (buttonResult[0]) {
                        mStampFragment.setState(buttonResult[0],0)
                    }
                    1 -> if (buttonResult[1]) {
                        mStampFragment.setState(buttonResult[1],1)
                    }
                    2 -> if (buttonResult[2]) {
                        mStampFragment.setState(buttonResult[2],2)
                    }
                    3 -> if (buttonResult[3]) {
                        mStampFragment.setState(buttonResult[3],3)
                    }
                    4 -> if (buttonResult[4]) {
                        mStampFragment.setState(buttonResult[4],4)
                    }
                    5 -> if (buttonResult[5]) {
                        mStampFragment.setState(buttonResult[5],5)
                    }
                }
            }
        }
    }





}

