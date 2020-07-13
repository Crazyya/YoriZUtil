package com.yoriz.yorizutil.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yoriz
 * on 2019/2/20 3:40 PM.
 */
class UniversalAdapter<T> : RecyclerView.Adapter<UniversalViewHolder> {
    var mData: List<T>
    private var mOnBindInterface: OnBindDataInterface<T>
    private var mOnMultiTypeBindDataInterface: OnMultiTypeBindDataInterface<T>? = null

    /**
     * 单种ViewHolder的初始化方法
     */
    constructor(data: List<T>, bindInterface: OnBindDataInterface<T>) {
        this.mData = data
        this.mOnBindInterface = bindInterface
    }

    /**
     * 支持多类型ViewHolder的初始化方法
     */
    constructor(data: List<T>, bindInterface: OnMultiTypeBindDataInterface<T>) {
        this.mData = data
        this.mOnBindInterface = bindInterface
        this.mOnMultiTypeBindDataInterface = bindInterface
    }

    override fun getItemViewType(position: Int): Int {
        return if (mOnMultiTypeBindDataInterface != null) {
            mOnMultiTypeBindDataInterface!!.getItemViewType(position)
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversalViewHolder {
        val layoutId = mOnBindInterface.getItemLayoutId(viewType)
        return UniversalViewHolder.getViewHolder(parent, layoutId)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: UniversalViewHolder, position: Int) {
        mOnBindInterface.onBindData(mData[position], holder, getItemViewType(position), position)
    }

}