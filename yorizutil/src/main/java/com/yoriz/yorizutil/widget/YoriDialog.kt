package com.yoriz.yorizutil.widget

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Created by yoriz
 * on 2019/4/1 12:12 PM.
 *
 * 伪建造者模式的Dialog
 * 支持Kotlin函数写法
 * 默认只监听Button类型控件点击事件
 * 如有其他点击需监听自行在setViewDetail中声明
 */
class YoriDialog : View.OnClickListener, YoriDialogManage {
    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog
    private var context: Context
    private var layout = 0
    private var click: YoriDialogClick? = null
    private var lambda: ((View, YoriDialogManage) -> Unit)? = null

    private constructor(context: Context, layout: Int, lambda: ((View, YoriDialogManage) -> Unit)?) {
        this.context = context
        this.layout = layout
        this.lambda = lambda
        setDialogView()
        setClickListener()
    }

    private constructor(context: Context, layout: Int, click: YoriDialogClick?) {
        this.context = context
        this.layout = layout
        this.click = click
        setDialogView()
        setClickListener()
    }

    /*******************************暴露给外部的方法*******************************************/

    /**
     * 设置dialogView上的各个详细数据,如adapter、rv此类
     */
    fun setViewDetail(setting: YoriDialogViewSetting) {
        setting.onSetView(dialogView)
        setting.onSetDialog(dialog)
    }

    /**
     * 设置dialogView上的各个详细数据,如adapter、rv此类
     * Kotlin专用lambda函数
     */
    fun setViewDetail(setting: (View) -> Unit) {
        setting(dialogView)
    }

    /**
     * 设置dialog属性
     */
    fun setDialogDetail(setting: (AlertDialog) -> Unit) {
        setting(dialog)
    }

    /**
     * 设置dialogView和dialog属性
     */
    fun setDetail(setting: (View, AlertDialog) -> Unit) {
        setting(dialogView, dialog)
    }

    override fun cancel() {
        dialog.cancel()
    }

    override fun show() {
        dialog.show()
    }

    /**********************************暴露给外部的方法****************************************/
    private fun setDialogView() {
        dialogView = View.inflate(context, layout, null)
    }

    private fun setDialog(isCancel: Boolean) {
        dialog = AlertDialog.Builder(context).let { builder ->
            builder.setView(dialogView)
            builder.setCancelable(isCancel)
            builder.create()
            builder.show()
        }
    }

    private fun setClickListener() {
        val viewList = getAllChildViews(dialogView)
        viewList.forEach {
            if (it is Button) {
                it.setOnClickListener(this)
            }
        }
    }

    private fun getAllChildViews(view: View): List<View> {
        val allChildren = ArrayList<View>()
        if (view is ViewGroup) {
            for (i in 0..view.childCount) {
                val child = view.getChildAt(i) ?: continue
                if (child is Button) {
                    allChildren.add(child)
                }
                allChildren.addAll(getAllChildViews(child))
            }
        }
        return allChildren
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (lambda != null) {
                lambda!!(v, this)
            } else if (click != null) {
                click!!.onClick(v, this)
            }
        }
    }

    /**
     * 写dialog监听的接口
     */
    interface YoriDialogClick {
        fun onClick(view: View, manager: YoriDialogManage)
    }

    /**
     * 写dialogView详细配置的接口
     */
    interface YoriDialogViewSetting {
        fun onSetView(view: View)
        fun onSetDialog(dialog: AlertDialog)
    }

    /**
     * 配置产生dialogUtil的类
     */
    class Builder(context: Context,
                  private val layout: Int) {
        private val applicationContext = context
        private var isCancel = true
        private var click: YoriDialogClick? = null
        /**
         * Kotlin专用lambda函数对象
         */
        private var lambdaClick: ((View, YoriDialogManage) -> Unit)? = null

        /**
         * Kotlin专用lambda函数
         */
        fun setOnClickListener(click: ((View, YoriDialogManage) -> Unit)): Builder {
            lambdaClick = click
            return this
        }

        fun setOnClickListener(click: YoriDialogClick): Builder {
            this.click = click
            return this
        }

        fun setCancelable(isCancel: Boolean): Builder {
            this.isCancel = isCancel
            return this
        }

        /**
         * 创建Dialog
         */
        fun createDialog(): YoriDialog {
            val result = if (lambdaClick != null) {
                YoriDialog(applicationContext, layout, lambdaClick)
            } else {
                YoriDialog(applicationContext, layout, click)
            }
            result.setDialog(isCancel)
            return result
        }
    }
}


interface YoriDialogManage {
    fun cancel()
    fun show()
}