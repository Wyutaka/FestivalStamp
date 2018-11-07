package com.example.nakatsuka.newgit.NavigationAction

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.nakatsuka.newgit.MainAction.MainActivity
import com.example.nakatsuka.newgit.MainAction.SecondActivity
import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.fragment_first.*

//イベントactivity内でviewを呼び出す方法

class FirstFragment : Fragment() {
    private var quizNumber:Int = 0

    interface FragmentListener{
        fun onClickButton()
    }

    private var mListener: FragmentListener? = null

    override fun onAttach(context: Context){
        super.onAttach(context)

        if (context is FragmentListener){
            mListener = context
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_first, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view!!.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            if(mListener != null){
                mListener?.let { it.onClickButton() }


            }
        }
        /*imageButton1.setOnClickListener {
            if(mListener != null){
                mListener.onClickButton()
            }
            quizNumber = 0
            setQuizNumber(quizNumber)
        }*/
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

    override fun onDetach() {
        super.onDetach()
        mListener =null
    }
}


