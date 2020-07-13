package com.yoriz.yorizutil.mvp

/**
 * Created by yoriz
 * on 2018/12/18 5:16 PM.
 * CallBack类监听监听接口
 *
 */
interface YoriListener {
    fun listenerSuccess(data: Any)
    fun listenerFailed(data: Any)
    fun listenerOther(data: Any) {}
}