package com.example.nakatsuka.newgit.navigationAction

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.nakatsuka.newgit.R
import android.os.Build
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.fragment_map.*
import org.xwalk.core.XWalkView
import org.xwalk.core.internal.XWalkWebChromeClient


class MapFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_map, container, false)

        val mwebview = view.findViewById(R.id.webView) as WebView
        mwebview.loadUrl("https://c0de-dev.club.nitech.ac.jp/knoom-web/map",null)


        val webSettings = mwebview.settings
        webSettings.javaScriptEnabled = true

        mwebview.webViewClient = WebViewClient()
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true


        return view
    }
}
