package com.yoriz.yorizutil.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Created by yoriz
 * on 2019-09-23 12:26.
 *
 * viewModel 为了使用方便，声明MutableLiveData的时候可以不用private和protected，但是注意不要在除ViewModel中直接set
 * 这么做主要为了方便使用的时候.observe和获取值
 * 正确做法应该就是BaseViewModel不提供变量给外部只暴露方法出去
 */
abstract class BaseViewModel(protected val savedStateHandle: SavedStateHandle) : ViewModel() {

    data class ErrorResult(
            val error: Any,
            val func: () -> Unit = {}
    )

    protected val showDialog = MutableLiveData<Boolean>()

    val msg = MutableLiveData<Any>()

    val error = MutableLiveData<ErrorResult>()

    fun getShowDialog(lifecycle: () -> Lifecycle, observe: (Boolean) -> Unit) {
        showDialog.observe(lifecycle, observe)
    }

    fun getError(lifecycle: () -> Lifecycle, observe: (ErrorResult) -> Unit) {
        error.observe(lifecycle, observe)
    }

    fun getMsg(lifecycle: () -> Lifecycle, observe: (Any) -> Unit) {
        msg.observe(lifecycle, observe)
    }

    /**
     * 供继承BaseViewModel的使用，更偷懒了，直接用类的SavedStateHandle
     */
    protected fun <T> MutableLiveData<T>.init(keyName: String, defaultValue: T? = null): MutableLiveData<T> {
        return this.also {liveData->
            //确认是否包含此key
            if (!savedStateHandle.contains(keyName)) {
                //如果不包含就要写入值，在这之前确认传递进来的值是否为null，null就结束
                defaultValue?.also {
                    savedStateHandle.set(keyName, it)
                    liveData.value = it
                }
            } else {
                //包含对应的值就直接赋值
                liveData.value = savedStateHandle.get(keyName)
            }
        }
    }

    /**
     * 供继承BaseViewModel的使用，更偷懒了，直接用类的SavedStateHandle
     */
    protected fun <T> MutableLiveData<T>.set(keyName: String, value: T?) {
        value?.also {
            this.value = it
            savedStateHandle.set(keyName, it)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

/**
 * 供非继承BaseViewModel的写入使用
 * @param savedStateHandle 不知道里面是啥的可以别当开发了
 */
fun <T> MutableLiveData<T>.initSavedState(savedStateHandle: SavedStateHandle, keyName: String, defaultValue: T? = null): MutableLiveData<T> {
    return this.also {
        //确认是否包含此key
        if (!savedStateHandle.contains(keyName)) {
            //如果不包含就要写入值，在这之前确认传递进来的值是否为null，null就结束
            if (defaultValue == null) return@also
            savedStateHandle.set(keyName, defaultValue)
        } else {
            //包含对应的值就直接赋值
            it.value = savedStateHandle.get(keyName)
        }
    }
}

/**
 * 供非继承BaseViewModel的写入使用
 * @param savedStateHandle 不知道里面是啥的可以别当开发了
 */
fun <T> MutableLiveData<T>.setSavedStateAndValue(savedStateHandle: SavedStateHandle, keyName: String, value: T?) {
    this.value = value
    savedStateHandle.set(keyName, value)
}