package com.c0de_mattari.nitstamprally.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.c0de_mattari.nitstamprally.R
import com.c0de_mattari.nitstamprally.mainAction.controller.api.ApiController
import kotlinx.android.synthetic.main.activity_second.*

const val result_canceled: Int = 3

//todo ここだけキーボードが背景タッチで消えない

class SecondActivity : AppCompatActivity() {

    private lateinit var inputMethodManager: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //MainActivityからのインテントと仮APIのデータを受け取る
        val intent = intent
        //このあたりでbuttonNumberから画像の設定

        //正誤判定
        val mApiController = ApiController()
        var answerResult = false
        val prefer: SharedPreferences = getSharedPreferences("prefer", Context.MODE_PRIVATE)
        val uuid = prefer.getString("UUID", "")
        var quizCode = intent.getIntExtra("AnswerNumber", 0)

        when (quizCode) {
            1 -> question_image.setImageResource(R.drawable.quiz1)
            2 -> question_image.setImageResource(R.drawable.quiz2)
            3 -> question_image.setImageResource(R.drawable.quiz3)
            4 -> question_image.setImageResource(R.drawable.quiz4)
            5 -> question_image.setImageResource(R.drawable.quiz5)
            6 -> question_image.setImageResource(R.drawable.quiz6)
        }

        question_number.text = "謎解き$quizCode"
        answer_button.setOnClickListener {
            /**
             * サーバー側の正誤判定に合わせるため正解がquizCodeごとに変わります
             * 1:"AD34E" 2:"DEACG" 3:"FSXJW" 4:"VX8LK" 5:"X1QPY" 6:"HIQ3A"
             */

            mApiController.judgeAnswer(this, uuid, quizCode, answer_word.editableText.toString()) { response ->
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            answerResult = it.isCorrect
                            judgement(answerResult, quizCode, response.code(), "")
                        }
                    }
                    401 -> {
                        response.body()?.let {
                            judgement(answerResult, quizCode, 401, "")
                        }
                    }
                    else -> {
                        judgement(answerResult, quizCode, response.code(), "")
                    }
                }
            }
        }

        back_button.setOnClickListener {
            setResult(result_canceled)
            finish()
        }

    }

    private fun judgement(answerResult: Boolean, quizCode: Int, responseCode: Int, msg: String) {
        if (responseCode == 200) {
            if (answerResult) {
                AlertUtil.showNotifyDialog(this,"正解！","スタンプを押します")
                {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("isCorrect", answerResult)
                    intent.putExtra("answerNumber", quizCode)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                /*
                AlertDialog.Builder(this)
                        .setTitle("正解!")
                        .setMessage("スタンプを押します")
                        .setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("isCorrect", answerResult)
                            intent.putExtra("answerNumber", quizCode)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        .show()
                        */
            } else {
                AlertUtil.showNotifyDialog(this,"不正解","もう一度考えてみて下さい")
                /*
                AlertDialog.Builder(this)
                        .setTitle("不正解")
                        .setMessage("もう一度考えてみてください")
                        .setPositiveButton("OK") { _, _ ->
                        }.show()
                        */
            }
        } else {
            when (responseCode) {
                400 -> {
                    AlertUtil.showNotifyDialog(this,"エラー","解答を入力し直して下さい")
                    /*
                    AlertDialog.Builder(this)
                            .setTitle("エラー")
                            .setMessage("解答を入力し直してください")
                            .setPositiveButton("OK") { _, _ ->
                            }.show()
                            */
                }
                401 -> {
                    if (msg == "Not Send UUID") {
                        AlertUtil.showNotifyDialog(this,"通信エラー","もう一度やり直して下さい")

                        /*
                        AlertDialog.Builder(this)
                                .setTitle("通信エラー")
                                .setMessage("もう一度やり直してください")
                                .setPositiveButton("OK") { _, _ ->
                                }.show()
                                */
                    } else if (msg == "User not found") {
                        AlertUtil.showNotifyDialog(this,"エラー","このエラーが出続けるよう場合、お手数ですがサービスセンターにお越しください")
                        /*
                        AlertDialog.Builder(this)
                                .setTitle("エラー")
                                .setMessage("このエラーが出続けるよう場合、お手数ですがサービスセンターにお越しください")
                                .setPositiveButton("OK") { _, _ ->
                                }.show()
                                */
                    }
                }
                500 -> {
                    AlertUtil.showNotifyDialog(this,"エラー","解答を入力し直して下さい")
                    /*
                    AlertDialog.Builder(this)
                            .setTitle("エラー")
                            .setMessage("解答を入力し直してください")
                            .setPositiveButton("OK") { _, _ ->
                            }.show()
                */
                }
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        inputMethodManager.hideSoftInputFromWindow(for_focus2.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        for_focus2.requestFocus()
        return super.onTouchEvent(event)
    }


}
