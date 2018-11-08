package com.example.nakatsuka.newgit.mainAction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_second.*

const val result_canceled: Int = 3

//todo ここだけキーボードが背景タッチで消えない

class SecondActivity : AppCompatActivity() {

    lateinit private var inputMethodManager: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //MainActivityからのインテントと仮APIのデータを受け取る
        val intent = intent
        val answerNumber = intent.getIntExtra("AnswerNumber", 6)
        val answernumberforview = answerNumber+1

        question_number.setText("謎解き" + answernumberforview.toString())
        val APITest = APITest()
        APITest.setButtonNumber(answerNumber)
        val APIData: String = APITest.getAPIData()


        //テストのためMainActivityからの情報を表示
        result.text = APIData
        //このあたりでbuttonNumberから画像の設定


        //正誤判定


        var answerResult = false
        answer_button.setOnClickListener {
            val rightAnswer = "126"
            //EditTextからの答えをセット
            val answer = answer_word.editableText.toString()
            if (answer == rightAnswer) {
                answerResult = true
                judgement(answerResult, answerNumber)
            } else {
                judgement(answerResult, answerNumber)
            }
        }

        back_button.setOnClickListener {
            setResult(result_canceled)
            finish()
        }
    }


    private fun judgement(answerResult: Boolean, answerNumber: Int) {
        if (answerResult) {
            AlertDialog.Builder(this)
                    .setTitle("正解!")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("isCorrect", answerResult)
                        intent.putExtra("answerNumber", answerNumber)
                        setResult(RESULT_OK, intent)
                        finish()

                    }
                    .show()
        } else {
            AlertDialog.Builder(this)
                    .setTitle("不正解!")
                    .setPositiveButton("OK") { _, _ ->
                    }.show()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        inputMethodManager.hideSoftInputFromWindow(for_focus2.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        for_focus2.requestFocus()
        return super.onTouchEvent(event)
    }


}
