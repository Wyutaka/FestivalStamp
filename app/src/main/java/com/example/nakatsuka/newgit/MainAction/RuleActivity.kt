package com.example.nakatsuka.newgit.MainAction

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_rule.*

class RuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule)

        agree_contents.setOnClickListener {
            val intent = Intent(this,ResistActivity::class.java)
            startActivity(intent)
        }



    }
}
