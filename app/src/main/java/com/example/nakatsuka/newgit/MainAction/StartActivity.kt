package com.example.nakatsuka.newgit.MainAction

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.nakatsuka.newgit.BuildConfig
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        val button = start_button

        button.setOnClickListener{
            val intent = Intent(this, RuleActivity::class.java)
            startActivity(intent)
        }
    }
}
