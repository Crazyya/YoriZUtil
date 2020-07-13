package com.yoriz.yorizutil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

/**
 * Created by yoriz
 * on 2018/12/18 12:25 PM.
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    companion object {
        // 应用退出用的广播标签
        private const val EXIT_APP_ACTION = "finish"
    }

    /**
     * 退出应用使用的广播
     */
    private val exitBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == EXIT_APP_ACTION && context is BaseActivity) {
                context.finish()
            }
        }
    }

    // 使用户不能超速进入返回，跳转动画没做完就返回次数多了视觉上会觉得卡的
    private var isBack = false

    /**
     * 此处是最下方虚拟按键的背景色
     */
    protected var footNavigationBarColor = android.R.color.black

    /**
     * 布局
     */
    protected abstract val layoutId: Int

    /**
     * 初始化各项数据，执行在onCreate中
     */
    protected abstract fun initCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, footNavigationBarColor)
        }
        super.onCreate(savedInstanceState)
        //获得布局
        setContentView(layoutId)
        baseOnCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction(EXIT_APP_ACTION)
        registerReceiver(exitBroadcastReceiver, intentFilter)
        initCreate()
    }

    /**
     * 给子类在onCreate执行完运行的接口
     */
    protected fun baseOnCreate() {

    }

    override fun onStart() {
        super.onStart()
        // 每次进入应用都有0.8s时间后才允许退出
        isBack = false
        launch {
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
        //这里会出现一个取消异常，资料确认是取消手段，加个catch捕获了，正确应该捕获CancellationException,这里直接用这个了
        try{
            cancel()
        }catch (e:Exception){}
        // 释放广播监听
        unregisterReceiver(exitBroadcastReceiver)
        super.onDestroy()
    }

    /**
     * 退出应用
     */
    protected fun exitAPP() {
        try {
            val intent = Intent()
            intent.action = EXIT_APP_ACTION
            sendBroadcast(intent)
            ActivityCompat.finishAfterTransition(this)
        } catch (e: Exception) {
            finish()
        }
    }
}