package com.yoriz.yorizutil.mvvm

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import com.yoriz.yorizutil.BaseActivity
import com.yoriz.yorizutil.R
import com.yoriz.yorizutil.widget.YoriDialog
import com.yoriz.yorizutil.widget.YoriLoadingDialog
import kotlinx.android.synthetic.main.dialog_text.view.*

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

    protected abstract fun showError(data: Any, func: () -> Unit = {})

    protected abstract fun showMsg(data: Any)
}