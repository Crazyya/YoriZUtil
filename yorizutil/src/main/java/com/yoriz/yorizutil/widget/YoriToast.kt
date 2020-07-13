package com.yoriz.yorizutil.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Created by yoriz
 * on 2018/12/18 11:43 AM.
 */
object YoriToast {
    private var toast: Toast? = null
    private var handler = Handler(Looper.getMainLooper())

    @SuppressLint("ShowToast")
    @JvmStatic
    fun showShortToast(context: Context, msg: String) {
        if (toast == null) {
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
        }
        toast!!.setText(msg)
        toast!!.duration = Toast.LENGTH_SHORT

        handler.post { toast!!.show() }
    }

    @SuppressLint("ShowToast")
    @JvmStatic
    fun showLongToast(context: Context, msg: String) {
        if (toast == null) {
            toast = Toast.makeText(context, null, Toast.LENGTH_LONG)
        }
        toast!!.setText(msg)
        toast!!.duration = Toast.LENGTH_SHORT

        handler.post { toast!!.show() }
    }
}