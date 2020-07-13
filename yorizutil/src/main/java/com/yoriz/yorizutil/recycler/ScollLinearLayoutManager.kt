package com.yoriz.yorizutil.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by yoriz
 * on 2019/4/2 3:06 PM.
 */
class ScrollLinearLayoutManager : LinearLayoutManager {

    private var isScrollEnabled = true

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }


    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}