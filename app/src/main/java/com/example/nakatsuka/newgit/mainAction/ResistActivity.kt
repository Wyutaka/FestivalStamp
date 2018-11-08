package com.example.nakatsuka.newgit.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import kotlinx.android.synthetic.main.activity_resist.*

class ResistActivity : AppCompatActivity() {

    lateinit private var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resist)

        //キーボードの設定
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val connection = false
        val used = false


        //ユーザー名の登録とUUIDの作成、またその保存
        send_name.setOnClickListener {
            val userName: String = userName.editableText.toString()

            AlertDialog.Builder(this)
                    .setTitle("確認")
                    .setMessage(userName + "で登録します")
                    .setPositiveButton("OK") { _, _ ->


                        //ここにprogressBar

                        val mApiController = ApiController()

                        //val APITest = APITest()
                        //APITest.device = Build.DEVICE
                        var stringUUID = ""

                        //TODO 実装例
                        mApiController.registerUser(userName, Build.DEVICE, Build.VERSION.RELEASE) { response ->
                            when (response.code()) {
                                200 -> {
                                    AlertDialog.Builder(this)
                                            .setTitle("登録完了")
                                            .setPositiveButton("OK") { _, _ ->

                                            }
                                            .show()
                                    response.body()?.let {
                                        stringUUID = it.uuid

                                        //APITest.UUID = it.uuid
                                        //APITest.userName = userName

                                        Log.d("registerUser", "${response.body()}, stringUUID: $stringUUID")
                                    }

                                    Log.d("UUID", stringUUID)
                                    val prefer: SharedPreferences = getSharedPreferences("prefer", Context.MODE_PRIVATE)
                                    val editor: SharedPreferences.Editor = prefer.edit()
                                    editor.putString("UUID", stringUUID)
                                    editor.putString("USERNAME", userName)
                                    editor.commit()
                                    val intent = Intent(this, MainActivity::class.java)

                                    //intent.putExtra("APITest", APITest)

                                    startActivity(intent)
                                }
                                400 -> {
                                    AlertDialog.Builder(this)
                                            .setTitle("エラー")
                                            .setMessage("5文字以上で入力してください")
                                            .setPositiveButton("OK") { _, _ ->
                                            }
                                            .show()
                                    Log.e("registerUser", "${response.code()}, ${response.body()}")
                                }
                                409 -> {
                                    AlertDialog.Builder(this)
                                            .setTitle("エラー")
                                            .setMessage("その名前は使用済みです")
                                            .setPositiveButton("OK") { _, _ ->
                                            }
                                            .show()

                                }
                                500 -> {

                                    AlertDialog.Builder(this)
                                            .setTitle("通信エラー")
                                            .setMessage("もう一度やり直してください")
                                            .setPositiveButton("OK") { _, _ ->
                                            }
                                            .show()
                                }
                            }
                        }


                    }

                    .setNegativeButton("cancel") { _, _ ->
                    }

                    .show()

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        inputMethodManager.hideSoftInputFromWindow(for_focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        for_focus.requestFocus()
        return super.onTouchEvent(event)
    }
}
