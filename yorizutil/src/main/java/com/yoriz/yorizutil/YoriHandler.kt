package com.yoriz.yorizutil

import android.os.Handler
import android.os.Message
import com.yoriz.yorizutil.mvp.YoriListener

/**
 * Created by zhiya.zhang
 * on 2017/8/28 17:04.
 *
 * 2017/10/11修改后启用,增加外部handler
 * 类本身就是一个handler，和内部的静态handler区别在于，内部handler是全局唯一，相当于static final，
 * MyHandler相当于只是一个对象，用来new一个handler使用，因此不像内部handler会自动处理防止泄漏
 * 内部handler用于socket，外部handler用于其他
 *
 * 2018-06-12修改，简简单单就好，删代码！
 *
 * 2018-11-16 RX大法好，感觉用不着handler了啊
 *
 * 2019-04-01 RX大法好，但是有的地方还是Handler用得爽
 */
class YoriHandler : Handler() {

    companion object {
        const val SUCCESS = 0
        const val ERROR = 1
        const val OTHER = 2
    }

    private var isRun: Boolean = false
    private var mListener: YoriListener? = null

    fun writeListener(myListener: YoriListener): YoriHandler {
        mListener = myListener
        isRun = true
        return this
    }

    fun cleanAll() {
        cleanAL()
        this.removeCallbacksAndMessages(null)
    }

    private fun cleanAL() {
        if (!isRun) {//还在运行就不允许清空
            mListener = null
        }
    }

    override fun handleMessage(msg: Message) {
        try {
            if (mListener != null) {
                isRun = when (msg.what) {
                    SUCCESS -> {
                        mListener!!.listenerSuccess(msg.obj)
                        false
                    }
                    ERROR -> {
                        mListener!!.listenerFailed(msg.obj as String)
                        false
                    }
                    OTHER -> {
                        mListener!!.listenerOther(msg.obj)
                        false
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            isRun = false
            if (e.message != null) {
                try {
                    mListener!!.listenerFailed(e.message.toString())
                } catch (e: Exception) {
                    throw e
                }
            }
            return
        }
    }
}