package com.c0de_mattari.nitstamprally.mainAction

import android.app.Activity
import android.support.v7.app.AlertDialog

class AlertUtil {
    companion object {
        fun showYesNoDialog(activity: Activity, title: String = "", message: String = "", cancelFlag: Boolean = false, callback: () -> Unit = {}) {
            if (activity != null && !activity.isFinishing && !activity.isDestroyed) {

                AlertDialog.Builder(activity).apply {
                    if (!title.isNullOrEmpty()) {
                        setTitle(title)
                    }
                    if (!message.isNullOrEmpty()) {
                        setMessage(message)
                    }
                    setPositiveButton("OK") { _, _ ->
                        callback.invoke()
                    }
                    if (cancelFlag) {
                        setNegativeButton("cancel") { _, _ ->
                        }
                    }
                }.show()
            }
        }

        fun showNotifyDialog(activity: Activity, title: String = "", message: String = "", callback: () -> Unit = {}) = showYesNoDialog(activity, title, message, false, callback)
    }
}