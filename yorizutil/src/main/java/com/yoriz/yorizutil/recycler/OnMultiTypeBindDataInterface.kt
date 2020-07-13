package com.yoriz.yorizutil.recycler

/**
 * Created by yoriz
 * on 2019/2/20 5:04 PM.
 * 对ViewHolder进行多类型支持
 */
interface OnMultiTypeBindDataInterface<T> : OnBindDataInterface<T> {
    fun getItemViewType(position: Int): Int
}