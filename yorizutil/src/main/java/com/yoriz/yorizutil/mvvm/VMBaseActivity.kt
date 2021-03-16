package com.yoriz.yorizutil.mvvm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.text.method.ScrollingMovementMethod
import androidx.lifecycle.lifecycleScope
import com.yoriz.yorizutil.BaseActivity
import com.yoriz.yorizutil.R
import com.yoriz.yorizutil.widget.YoriDialog
import com.yoriz.yorizutil.widget.YoriLoadingDialog
import kotlinx.android.synthetic.main.dialog_text.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yoriz
 * on 2019-09-23 12:12.
 */
abstract class VMBaseActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract fun initViewModel(): VM

    protected open val loadingDialog: YoriLoadingDialog? by lazy {
        YoriLoadingDialog(this)
    }

    protected open val promptDialog: YoriDialog by lazy {
        YoriDialog.Builder(this, R.layout.dialog_text).let {
            it.createDialog().apply {
                setDialogDetail { alertDialog ->
                    alertDialog.setCanceledOnTouchOutside(true)
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    cancel()
                }
                setViewDetail{
                    it.msg.movementMethod = ScrollingMovementMethod.getInstance()
                }
            }
        }
    }

    protected fun showPromptDialog(msg: String) {
        promptDialog.setViewDetail {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.msg.text = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY)
            } else {
                it.msg.text = Html.fromHtml(msg)
            }
        }
        promptDialog.show()
    }

    protected val vm: VM by lazy {
        initViewModel()
    }

    override fun initCreate() {
        vm.getShowDialog({ lifecycle }, {
            if (it) {
                loadingDialog?.show()
            } else {
                loadingDialog?.dismiss()
            }
        })
        vm.getError({ lifecycle }, {
            showError(it.error, it.func)
        })
        vm.getMsg({ lifecycle }, {
            showMsg(it)
        })
    }

    protected open fun showError(data: Any, func: () -> Unit = {}) {
        showPromptDialog(data.toString())
    }

    protected open fun showMsg(data: Any){
        showPromptDialog(data.toString())
    }



    // 使用户不能超速进入返回，跳转动画没做完就返回次数多了视觉上会觉得卡的
    private var isBack = false

    override fun onStart() {
        super.onStart()
        // 每次进入应用都有0.8s时间后才允许退出
        isBack = false
        lifecycleScope.launch {
            delay(800)
            isBack = true
        }
        super.onStart()
    }

    override fun onBackPressed() {
        if (isBack) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        // 释放广播监听
        unregisterReceiver(exitBroadcastReceiver)
        super.onDestroy()
    }
}