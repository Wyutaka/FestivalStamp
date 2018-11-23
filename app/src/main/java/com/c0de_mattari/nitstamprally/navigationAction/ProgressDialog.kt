package com.c0de_mattari.nitstamprally.navigationAction

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.c0de_mattari.nitstamprally.R

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
        builder.setNegativeButton("CANCEL", { _, _ ->
            this.brk = false
        })
        val inflater = activity!!.layoutInflater

        val inf = inflater.inflate(R.layout.progressdialog, null)

        builder.setView(inf)
        this.isCancelable = false

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        mProgressBar = dialog.findViewById(R.id.progress)
        mProgressBar!!.scaleY = 0.1f
        mProgressMessage = dialog.findViewById(R.id.progress_message)
        mProgressMessage!!.text = mMessage
    }

    override fun show(manager: FragmentManager, tag: String) {
        super.show(manager, tag);
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
            try {
                mProgressBar?.progress = 10
                mProgressBar?.secondaryProgress = 16
                Thread.sleep(1000)
                mProgressBar?.progress = 20
                mProgressBar?.secondaryProgress = 28
                Thread.sleep(1000)
                mProgressBar?.progress = 30
            } catch (e: Exception) {
                Log.e("progress", e.message)
            }
        })
        thread.start()
    }
}