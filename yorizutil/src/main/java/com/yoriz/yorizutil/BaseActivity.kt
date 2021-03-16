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
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        // 应用退出用的广播标签
        private const val EXIT_APP_ACTION = "finish"
    }

    /**
     * 退出应用使用的广播
     */
    protected val exitBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == EXIT_APP_ACTION && context is BaseActivity) {
                context.finish()
            }
        }
    }

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