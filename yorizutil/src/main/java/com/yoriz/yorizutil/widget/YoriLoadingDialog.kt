package com.yoriz.yorizutil.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yoriz.yorizutil.DensityUtil
import com.yoriz.yorizutil.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Created by yoriz
 * on 2019-09-19 15:33.
 */
class YoriLoadingDialog : Dialog {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, loadingGif: Int?) : super(context, R.style.YoriLoadingDialog) {
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_loading)
        Glide.with(context).load(loadingGif
                ?: R.mipmap.ic_yori_loading).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(loading_img)
        val lp = window?.attributes?.apply {
            width = DensityUtil.dip2px(context, 250f)
            height = DensityUtil.dip2px(context, 150f)
            gravity = Gravity.CENTER
        }
        window?.attributes = lp
    }
}