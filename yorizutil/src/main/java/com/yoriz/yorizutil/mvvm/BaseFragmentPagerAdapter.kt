package com.yoriz.yorizutil.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by yoriz
 * on 2020/6/10 5:24 PM.
 */
class BaseFragmentPagerAdapter : FragmentStateAdapter {

    private lateinit var mFragments: List<Fragment>

    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>) : super(fragmentActivity) {
        mFragments = fragments
    }

    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        mFragments = fragments
    }

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, fragments: List<Fragment>) : super(fragmentManager, lifecycle) {
        mFragments = fragments
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }
}