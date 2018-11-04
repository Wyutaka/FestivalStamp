package com.example.nakatsuka.newgit.NavigationAction

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.example.nakatsuka.newgit.R


class ThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_third, container, false)
        val mwebview = view.findViewById(R.id.webView) as WebView
        mwebview.loadUrl("https://www.nitech.ac.jp/")


        val webSettings = mwebview.getSettings()
        webSettings.javaScriptEnabled = true

        mwebview.webViewClient = WebViewClient()

        return view
    }

}
