package com.example.nakatsuka.newgit.mainAction

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import kotlinx.android.synthetic.main.activity_rule.*

class RuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule)

        val title = findViewById<TextView>(R.id.rule_title)
        val agreeButton = findViewById<Button>(R.id.agree_contents)
        val regulation = findViewById<TextView>(R.id.rule_contents)
        val mApiController = ApiController()
        mApiController.getRegulation(this,agreeButton, title) { response ->
            when (response.code()) {
                200 -> {
                    response.body()?.let {
                        regulation.text = it.ruleText
                    }
                }
            }
        }

        agree_contents.setOnClickListener {
            val intent = Intent(this, ResistActivity::class.java)
            startActivity(intent)
        }
    }

    fun jump(){
        finish()
    }
}
