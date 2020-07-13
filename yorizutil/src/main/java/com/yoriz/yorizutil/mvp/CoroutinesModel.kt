package com.yoriz.yorizutil.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by yoriz
 * on 2019/4/12 3:41 PM.
 */
class CoroutinesModel : CoroutineScope {
    var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}