package com.yoriz.yorizutil.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Created by yoriz
 * on 2019-09-23 12:12.
 */
abstract class VMBaseFragment<VM : BaseViewModel> : CoroutineScope by MainScope(), Fragment() {
    protected var rootView: View? = null

    protected abstract fun initViewModel(): VM

    protected abstract fun initView(view: View)

    protected val vm: VM by lazy {
        initViewModel()
    }

    /**
     * 布局
     */
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(layoutId, container, false)
        }
        val parent = rootView?.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(rootView)
        }
        initView(rootView!!)
        return rootView
    }

    override fun onDestroyView() {
        rootView = null
        super.onDestroyView()
    }
}