package com.yoriz.yorizutil.mvp

import android.content.Context
import com.yoriz.yorizutil.widget.YoriToast

/**
 * Created by yoriz
 * on 2019-04-29 08:32.
 */
interface GenView {


    /**
     * 调用toast工具弹提示
     */
    fun showPrompt(context: Context?, msg: String) {
        context?.let { YoriToast.showShortToast(it, msg) }
    }

    /**
     * 显示loading
     */
    fun showLoading() {}

    /**
     * 隐藏loading
     */
    fun hideLoading() {}
}