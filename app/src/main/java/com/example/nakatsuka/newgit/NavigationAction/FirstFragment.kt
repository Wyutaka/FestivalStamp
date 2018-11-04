package com.example.nakatsuka.newgit.NavigationAction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment() {
    private var quizNumber:Int = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_first, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("firstdesu","firstdesu")
        imageButton1.setOnClickListener {
            quizNumber = 0
            setQuizNumber(quizNumber)
        }
        Log.d("firstafter","firstafter")
        imageButton2.setOnClickListener {
            quizNumber = 1
            setQuizNumber(quizNumber)
        }
        imageButton3.setOnClickListener {
            quizNumber = 2
            setQuizNumber(quizNumber)
        }
        imageButton4.setOnClickListener {
            quizNumber = 3
            setQuizNumber(quizNumber)
        }
        imageButton5.setOnClickListener {
            quizNumber = 4
            setQuizNumber(quizNumber)
        }
        imageButton6.setOnClickListener {
            quizNumber = 5
            setQuizNumber(quizNumber)
        }
    }

    private fun setQuizNumber(quizNumber:Int){
        this.quizNumber = quizNumber
    }
    fun getQuizNumber():Int {
        return quizNumber
        quizNumber = 6
    }


}


