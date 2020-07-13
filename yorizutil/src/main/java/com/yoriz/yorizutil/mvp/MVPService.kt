package com.yoriz.yorizutil.mvp

import android.app.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

/**
 * Created by yoriz
 * on 2019-05-05 09:07.
 * 和mvpActivity一样
 */
abstract class MVPService : Service(), CoroutineScope by MainScope() {
    private var basePresenter: BasePresenter? = null

    protected abstract fun bindPresenter(): BasePresenter

    override fun onCreate() {
        super.onCreate()
        basePresenter = bindPresenter()
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        basePresenter = null
        //coroutine cancel的时候会发送CancelException，需要捕获一下
        try{
            if (isActive) cancel()
        }catch(e:Exception){

        }
        super.onDestroy()
    }
}