package com.c0de_mattari.nitstamprally.navigationAction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.c0de_mattari.nitstamprally.R

class ScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_schedule, container, false)
        val mwebview = view.findViewById(R.id.webView) as WebView
        mwebview.loadUrl("https://nitfes2018.ske.nitech.ac.jp/knoom-web/tt")


        val webSettings = mwebview.settings
        webSettings.javaScriptEnabled = true

        mwebview.webViewClient = WebViewClient()
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        return view
    }

}
