package com.yoriz.yorizutil.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yoriz
 * on 2018/12/18 3:11 PM.
 *
 * 各种滑动的
 */
class ItemTouchHelperCallback(private val moveAndSwipedOnListener: OnMoveAndSwipedListener, private val isAction: Boolean) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (isAction) {
            return if (recyclerView.layoutManager is LinearLayoutManager) {
                //单列的RecyclerView支持上下拖动和左右侧滑
                //改为不支持上下
                makeMovementFlags(
                        ItemTouchHelper.UP or
                                ItemTouchHelper.DOWN or
                                ItemTouchHelper.LEFT or
                                ItemTouchHelper.RIGHT,
                        ItemTouchHelper.START or
                                ItemTouchHelper.END)
            } else {
                //多列的RecyclerView支持上下左右拖动和不支持左右侧滑
                makeMovementFlags(
                        ItemTouchHelper.UP or
                                ItemTouchHelper.DOWN or
                                ItemTouchHelper.LEFT or
                                ItemTouchHelper.RIGHT,
                        0)
            }
        }
        return makeMovementFlags(0, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (!isAction) return false
        //如果两个item不是同一个类型的，不让他拖拽
        if (viewHolder.itemViewType != target.itemViewType) return false
        moveAndSwipedOnListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        moveAndSwipedOnListener.onItemDismiss(viewHolder.adapterPosition)
    }
}