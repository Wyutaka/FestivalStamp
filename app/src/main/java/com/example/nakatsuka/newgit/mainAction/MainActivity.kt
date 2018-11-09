package com.example.nakatsuka.newgit.mainAction

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.navigationAction.*
import kotlinx.android.synthetic.main.activity_main.*

/*Todo progressbarの処理
  Todo fragmentの処理　
  Todo APITestの部分の差し替え*/
class MainActivity : AppCompatActivity() {
    val RESULT_SUBACTIVITY: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("maindesu", "maindesu")


        //itimaie.webViewClient = WebViewClient()
        //itimaie.loadUrl("https://c0de-app.club.nitech.ac.jp/cloud/apps/files/?dir=/Public%20Share%20(C0de)/NitFes2018/www_knoom/design/stamp-picture&fileid=78384#/Public%20Share%20(C0de)/NitFes2018/www_knoom/design/stamp-picture/test.png")

        //fragmentの初期設定
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, StampFragment())
            transaction.commit()

            Log.d("fragmentdesu", "fragmentdesu")
        }

        val normal:Int = R.drawable.normalorengebutton
        val inverted:Int = R.drawable.invertedorangebutton
        //Todo アニメーションの追加
        stamp_rally.setOnClickListener {
            // コードからフラグメントを追加
            Log.d("fragment", "called!!")
            if (savedInstanceState == null) {
                stamp_rally.setBackgroundResource(inverted)
                map.setBackgroundResource(normal)
                board.setBackgroundResource(normal)
                time_table.setBackgroundResource(normal)
                others.setBackgroundResource(normal)
                val transaction = supportFragmentManager.beginTransaction()
                //バックスタックを設定
                transaction.addToBackStack(null)
                //パラメータを設定
                transaction.replace(R.id.container, StampFragment())
                transaction.commit()
            }
        }
        map.setOnClickListener {
            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(inverted)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
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
        board.setOnClickListener {
            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(inverted)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
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
            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(inverted)
            others.setBackgroundResource(normal)
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
            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(inverted)
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, OthersFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("アプリを終了しますか？")
                .setPositiveButton("OK") { _, _ ->
                    this.moveTaskToBack(true)
                }
                .setNegativeButton("キャンセル") { _, _ ->
                }
                .show()
        return super.onKeyDown(keyCode, event)
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
                        mStampFragment.setState(buttonResult[0], 0)
                    }
                    1 -> if (buttonResult[1]) {
                        mStampFragment.setState(buttonResult[1], 1)
                    }
                    2 -> if (buttonResult[2]) {
                        mStampFragment.setState(buttonResult[2], 2)
                    }
                    3 -> if (buttonResult[3]) {
                        mStampFragment.setState(buttonResult[3], 3)
                    }
                    4 -> if (buttonResult[4]) {
                        mStampFragment.setState(buttonResult[4], 4)
                    }
                    5 -> if (buttonResult[5]) {
                        mStampFragment.setState(buttonResult[5], 5)
                    }
                }
            }
        }
    }


}

