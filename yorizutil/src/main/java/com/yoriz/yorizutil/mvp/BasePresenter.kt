package com.yoriz.yorizutil.mvp

/**
 * Created by yoriz
 * on 2018/12/18 11:40 AM.
 */
interface BasePresenter {
    /**
     * 需要在此处清空持有的view
     * 在5.0后调用System.gc()已无效，因此没必要调用
     * 如果非要强行调用gc回收：
     * 1.System.gc()和System.runFinalization()同时调用
     * 2.直接调用Runtime.getRuntime().gc()
     */
    fun onDestroy()
}