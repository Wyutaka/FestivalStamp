package com.example.nakatsuka.newgit.navigationAction

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.nakatsuka.newgit.R
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_license.*

class LicenseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //shut_license.setOnClickListener {

        //}

        return inflater.inflate(R.layout.fragment_license, container, false)
    }
}
