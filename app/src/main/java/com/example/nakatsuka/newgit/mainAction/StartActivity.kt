package com.example.nakatsuka.newgit.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.navigationAction.LicenseFragment
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }

        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_start)

        //プリファレンスからUUIDとuserNameを取得
        val prefer: SharedPreferences = getSharedPreferences("prefer", Context.MODE_PRIVATE)
        var stringUUID = prefer.getString("UUID", "")
        var userName = prefer.getString("USERNAME", "")

        //未登録の場合はAPITestにデバイス情報しか入りません
        //val APITest = APITest()
        //APITest.SetandPostUserJSON(userName, Build.DEVICE, stringUUID)

        //登録後はMainActivityにとばされます
        if (stringUUID != "" && userName != "") {
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra("APITest",APITest)
            startActivity(intent)
        }

        license_button.setOnClickListener {
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.license_container, LicenseFragment())
                //バックスタックを設定
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        start_button.setOnClickListener {
            val intent = Intent(this, RuleActivity::class.java)
            startActivity(intent)
        }

    }
}
