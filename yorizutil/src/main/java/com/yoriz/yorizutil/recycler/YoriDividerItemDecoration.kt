package com.yoriz.yorizutil.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yoriz.yorizutil.R

/**
 * Created by yoriz
 * on 2017/9/13 14:57.
 */

private const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
private const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
class YoriDividerItemDecoration(private val mContext: Context, orientation: Int) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mContext.resources.getDrawable(R.drawable.divider, null)
    } else {
        mContext.resources.getDrawable(R.drawable.divider)
    }
    private var mOrientation: Int = 0

    init {
        setOrientation(orientation)
    }

    /**
     * 设置分割线的显示样式
     *
     * @param resId drawable资源,可以使自定义的shape文件
     */
    fun setDivider(resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDivider = mContext.resources.getDrawable(resId, null)
        } else {
            mContext.resources.getDrawable(R.drawable.divider)
        }
    }

    private fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }


    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val v = RecyclerView(parent.context)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider.intrinsicWidth, 0)
        }
    }
}
