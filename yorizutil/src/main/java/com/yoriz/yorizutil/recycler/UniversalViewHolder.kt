package com.yoriz.yorizutil.recycler

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yoriz
 * on 2019/2/20 4:02 PM.
 * 通用行viewHolder
 */
class UniversalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        /**
         * 获得ViewHolder
         */
        fun getViewHolder(parent: ViewGroup, layoutId: Int): UniversalViewHolder {
            return UniversalViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
        }
    }

    /**
     * 子View集合
     */
    private val mViews: SparseArray<View> = SparseArray()
    private val mContentView: View = itemView

    /**
     * 这里应该用reified的，但是为了支持java只能不使用忽略掉return的提示了
     */
    fun <T : View> getSubView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = mContentView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view!! as T
    }
}