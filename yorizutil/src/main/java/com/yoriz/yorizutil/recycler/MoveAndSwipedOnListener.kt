package com.yoriz.yorizutil.recycler

/**
 * Created by zhiya.zhang
 * on 2017/6/2 17:14.
 */
interface OnMoveAndSwipedListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
}