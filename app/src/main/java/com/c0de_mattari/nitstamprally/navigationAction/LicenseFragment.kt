package com.c0de_mattari.nitstamprally.navigationAction

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.c0de_mattari.nitstamprally.R
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_license.*

class LicenseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_license, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shut_license.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }
    }
}
