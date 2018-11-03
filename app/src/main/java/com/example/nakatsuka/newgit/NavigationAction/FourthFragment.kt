package com.example.nakatsuka.newgit.NavigationAction

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.nakatsuka.newgit.R

class FourthFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_fourth, container, false)


    }

}
