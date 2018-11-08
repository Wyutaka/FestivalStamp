package com.example.nakatsuka.newgit.mainAction

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.controller.api.ApiController
import kotlinx.android.synthetic.main.activity_rule.*

class RuleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule)
        val regulation=findViewById<TextView>(R.id.rule_contents)
        val mApiController = ApiController()
        mApiController.getRegulation {
            mApiController.getRegulation { response ->
                response.body()?.let{
                    regulation.setText(it.ruleText)
                }
            }
        }


        agree_contents.setOnClickListener {
            val intent = Intent(this,ResistActivity::class.java)
            startActivity(intent)
        }



    }
}
