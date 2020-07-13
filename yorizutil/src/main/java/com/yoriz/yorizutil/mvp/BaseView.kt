package com.yoriz.yorizutil.mvp

/**
 * Created by yoriz
 * on 2019/4/8 3:35 PM.
 * View的通用接口方法
 */
interface BaseView<T> :GenView{
    /**
     * 请求成功
     */
    fun requestSuccess(responseData: T) {}

    /**
     * 请求失败
     */
    fun requestFail(responseData: String) {}
}