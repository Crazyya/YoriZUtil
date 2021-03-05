package com.yoriz.yorizutil.mvvm

/**
 * Created by yoriz
 * on 2020/6/10 3:51 PM.
 * 配合viewpager2使用
 */
abstract class VMLazyBaseFragment<VM : BaseViewModel> : VMBaseFragment<VM>() {
    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initData()
            isFirstLoad = false
        }
    }

    protected abstract fun initData()
}