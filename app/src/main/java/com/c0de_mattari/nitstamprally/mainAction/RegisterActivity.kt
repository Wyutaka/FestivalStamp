package com.c0de_mattari.nitstamprally.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.c0de_mattari.nitstamprally.R
import com.c0de_mattari.nitstamprally.mainAction.controller.api.ApiController
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit private var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //キーボードの設定
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //ユーザー名の登録とUUIDの作成、またその保存
        send_name.setOnClickListener {
            val userName: String = userName.editableText.toString()
            AlertUtil.showYesNoDialog(this,"確認","${userName}で登録します",true) {
                //ここにprogressBar

                val mApiController = ApiController()

                var stringUUID = ""

                mApiController.registerUser(this, userName, Build.DEVICE, Build.VERSION.RELEASE) { response ->
                    when (response.code()) {
                        200 -> {
                            AlertUtil.showNotifyDialog(this, "登録完了", callback = {
                                response.body()?.let {
                                    stringUUID = it.uuid

                                }

                                val prefer: SharedPreferences = getSharedPreferences("prefer", Context.MODE_PRIVATE)
                                val editor: SharedPreferences.Editor = prefer.edit()
                                editor.putString("UUID", stringUUID)
                                editor.putString("USERNAME", userName)
                                editor.apply()
                                val intent = Intent(this, MainActivity::class.java)

                                startActivity(intent)
                            })
                        }
                        400 -> {
                            AlertUtil.showNotifyDialog(this,"エラー","4文字以上で入力してください")
                        }
                        409 -> {
                            AlertUtil.showNotifyDialog(this,"エラー","その名前は利用できません")
                        }
                        500 -> {
                            AlertUtil.showNotifyDialog(this,"通信エラー","もう一度やり直して下さい")
                        }
                    }
                }
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        inputMethodManager.hideSoftInputFromWindow(for_focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        for_focus.requestFocus()
        return super.onTouchEvent(event)
    }
}
