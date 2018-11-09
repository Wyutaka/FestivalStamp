package com.example.nakatsuka.newgit.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //Todo launch画面でのCode画像
        //プリファレンスからUUIDとuserNameを取得
        val prefer:SharedPreferences = getSharedPreferences("prefer", Context.MODE_PRIVATE)
        var stringUUID = prefer.getString("UUID","")
        var userName = prefer.getString("USERNAME","")

        stringUUID = ""
        userName = ""

        //未登録の場合はAPITestにデバイス情報しか入りません
        val APITest = APITest()
        APITest.SetandPostUserJSON(userName, Build.DEVICE, stringUUID)

        //登録後はMainActivityにとばされます
        if(stringUUID != "" && userName != ""){
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("APITest",APITest)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        start_button.setOnClickListener{
            val intent = Intent(this, RuleActivity::class.java)
            startActivity(intent)
        }

    }
}
