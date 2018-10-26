package com.example.nakatsuka.newgit.MainAction

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.nakatsuka.newgit.BuildConfig
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_resist.*

class ResistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resist)


        send_name.setOnClickListener {
            val userName = userName.editableText.toString()
            val APITest = APITest()
            APITest.SetandPostUserJSON(userName, Build.DEVICE, BuildConfig.VERSION_NAME)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("APITest",APITest)
            startActivity(intent)
        }
    }
}
