package com.example.nakatsuka.newgit.navigationAction

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import com.example.nakatsuka.newgit.R

class ProgressDialog : DialogFragment() {

    var mProgressBar: ProgressBar? = null
    private var mProgressMessage: TextView? = null
    private var mStartedShowing: Boolean = false
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0

    private var mMessage: String? = null

    var brk = true

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mMessage = arguments!!.getString("message")

        val builder = AlertDialog.Builder(activity!!)
        builder.setNegativeButton("CANCEL") { _, _ ->
            brk = false
        }
        val inflater = activity!!.layoutInflater

        val inf = inflater.inflate(R.layout.progressdialog, null)

        builder.setView(inf)
        this.isCancelable = false

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        mProgressBar = dialog.findViewById(R.id.progress)
        mProgressBar!!.scaleY=0.1f
        mProgressMessage = dialog.findViewById(R.id.progress_message)
        mProgressMessage!!.text = mMessage
    }

    override fun show(manager: FragmentManager, tag: String) {
        mStartMillisecond = System.currentTimeMillis()
        mStartedShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = Handler()
        handler.postDelayed({
            if (mStopMillisecond > System.currentTimeMillis()) {
                showDialogAfterDelay(manager, tag)
            }
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogAfterDelay(manager: FragmentManager, tag: String) {
        mStartedShowing = true
        super.show(manager, tag)
    }

    private fun cancel() {
        mStopMillisecond = System.currentTimeMillis()

        if (mStartedShowing) {
            if (mProgressBar != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
    }

    private fun cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND.toLong() + SHOW_MIN_MILLISECOND.toLong()) {
            val handler = Handler()
            handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun cancelWhenNotShowing() {
        val handler = Handler()
        handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
    }

    companion object {
        private val DELAY_MILLISECOND = 450
        private val SHOW_MIN_MILLISECOND = 300

        fun newInstance(message: String): ProgressDialog {
            val instance = ProgressDialog()

            val arguments = Bundle()
            arguments.putString("message", message)
            instance.arguments = arguments

            return instance
        }
    }

    fun move() {
        mProgressBar?.max = 30
        val thread = Thread(Runnable {
            while(true) {
                try {
                    mProgressBar?.progress = 10
                    mProgressBar?.secondaryProgress= 16
                    Thread.sleep(1000)
                    mProgressBar?.progress = 20
                    mProgressBar?.secondaryProgress=28
                    Thread.sleep(1000)
                    if(!brk)
                        break
                    mProgressBar?.progress = 30
                    brk = true
                    break
                } catch (e: Exception) {
                }
            }
        })
        thread.start()
    }
}