package com.example.nakatsuka.newgit.navigationAction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.nakatsuka.newgit.R

class OthersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_others, container, false)
        val mwebview = view.findViewById(R.id.webView) as WebView
        mwebview.loadUrl("https://c0de-dev.club.nitech.ac.jp/knoom-web/other")


        val webSettings = mwebview.settings
        webSettings.javaScriptEnabled = true

        mwebview.webViewClient = WebViewClient()
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        return view
    }
}
