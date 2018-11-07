package com.example.nakatsuka.newgit.MainAction

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.nakatsuka.newgit.NavigationAction.FirstFragment
import com.example.nakatsuka.newgit.NavigationAction.SecondFragment
import com.example.nakatsuka.newgit.NavigationAction.ThirdFragment
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*

/*Todo 登録後の戻るボタンの制御
  Todo fragmentの処理　
  Todo 不要ボタン*/
class MainActivity : AppCompatActivity(),FirstFragment.FragmentListener {


    private val buttonResult = mutableListOf<Boolean>(false, false, false, false, false, false)
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
            transaction.replace(R.id.container, FirstFragment())
            transaction.commit()

            Log.d("fragmentdesu", "fragmentdesu")
        }
        /**
        //ボタンが押されるとそのボタン相応のquiznumが返されてきてボタンの反応があるときにだけgoActivityが呼び出されます
        val fragment = FirstFragment()
        val APITest: APITest = intent.extras.get("APITest") as APITest
        Log.d("maindesita","maindesita")
        val quizNumber =fragment.getQuizNumber()
        if(quizNumber in 0..5){
        goActivity(quizNumber)
        }


        //ナビゲーションに関するボタンの操作

         */
        stamp_rally.setOnClickListener {
            // コードからフラグメントを追加
            Log.d("fragment", "called!!")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //バックスタックを設定
                transaction.addToBackStack(null)
                //パラメータを設定
                transaction.replace(R.id.container, FirstFragment())
                transaction.commit()
            }
        }
        map.setOnClickListener {
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, SecondFragment())
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
                transaction.replace(R.id.container, ThirdFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }

        }
    }
        //イベントの情報
    override fun onClickButton() {
        Log.d("callback","called")
            goActivity(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_SUBACTIVITY) {
            if (resultCode == result_canceled) {
            } else if (resultCode == RESULT_OK) {


                val answerNumber: Int? = intent!!.getIntExtra("answerNumber", 6)
                buttonResult[answerNumber!!] = true

                when (answerNumber) {
                    0 -> if (buttonResult[0]) {
                        //imageButton1.setImageResource(0)
                    }
                    1 -> if (buttonResult[1]) {
                        imageButton2.setImageResource(0)
                    }
                    2 -> if (buttonResult[2]) {
                        imageButton3.setImageResource(0)
                    }
                    3 -> if (buttonResult[3]) {
                        imageButton4.setImageResource(0)
                    }
                    4 -> if (buttonResult[4]) {
                        imageButton5.setImageResource(0)
                    }
                    5 -> if (buttonResult[5]) {
                        imageButton6.setImageResource(0)
                    }
                }
            }
        }
    }

    private fun goActivity(answerNumber: Int) {
        val isAPI: Boolean
        val APITest = APITest()
        isAPI = APITest.getIsAPI()
        val intent = Intent(this, SecondActivity::class.java)
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
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, x, y / 4)
        toast.show()
    }


}

