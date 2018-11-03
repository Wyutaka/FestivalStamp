package com.example.nakatsuka.newgit.NavigationAction

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nakatsuka.newgit.R



class FirstFragment : Fragment() {

    companion object {
        private const val KEY_MAKER = "maker_name"
        private const val KEY_BRAND = "brand_name"

        fun createInstance(): FirstFragment {
            val firstfragment = FirstFragment()

            return firstfragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_first, container, false)

    }



}


