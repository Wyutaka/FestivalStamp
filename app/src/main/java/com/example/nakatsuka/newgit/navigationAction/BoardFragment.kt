package com.example.nakatsuka.newgit.navigationAction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.example.nakatsuka.newgit.R


class BoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_board, container, false)
        val mwebview = view.findViewById(R.id.webView) as WebView
        mwebview.loadUrl("https://nitfes2018.ske.nitech.ac.jp/knoom-web/bb")


        val webSettings = mwebview.settings
        webSettings.javaScriptEnabled = true

        mwebview.webViewClient = WebViewClient()
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        return view
    }

}
