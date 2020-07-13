package com.yoriz.yorizutil.recycler

/**
 * Created by yoriz
 * on 2019/2/20 4:01 PM.
 * 绑定RV的Adapter数据的接口
 * @param T model
 */
interface OnBindDataInterface<T> {
    /**
     * 把数据绑定到viewHolder的方法
     */
    fun onBindData(model: T, viewHolder: UniversalViewHolder, viewType: Int, position:Int)

    /**
     * 得到当前viewHolder的layoutId
     */
    fun getItemLayoutId(viewType: Int): Int
}