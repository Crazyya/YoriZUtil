package com.yoriz.yorizutil.mvp

import android.os.Bundle
import com.yoriz.yorizutil.BaseActivity

/**
 * Created by yoriz
 * on 2018/12/18 12:22 PM.
 * 只在使用MVP的页面使用此Activity，如不使用MVP则使用MyActivity
 * presenter必须继承BasePresenter配合使用
 */
abstract class MVPActivity : BaseActivity() {
    // activity持有私有presenter用于双方绑定onDestroy
    private var basePresenter: BasePresenter? = null

    /**
     * 获得当前presenter的对象，用于绑定私有presenter实体对象
     * @return 当前activity的presenter
     */
    protected abstract fun bindPresenter(): BasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        basePresenter = bindPresenter()
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        basePresenter = null
        super.onDestroy()
    }
}