package com.yoriz.dimmedlayerview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by yoriz
 * on 2019-05-20 10:09.
 */
class DimmedLayerView : View {

    companion object {
        private const val WIDTH_RATIO = 16
        private const val HEIGHT_RATIO = 9
        private const val DIMMED_ALPHA = 100
        private val DIMMED_COLOR = Color.parseColor("#5fC0C0C0")
    }

    private lateinit var mCenterRectPath: Path
    private var mCenterRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var mDimmedPath: Path
    private var mDimmedPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //中间抠图层的坐标，可以结合显示图片的view进行剪裁
    var mCenterRect = RectF()
        private set

    //默认的宽高比
    var widthRatio = WIDTH_RATIO
        private set(value) {
            field = if (value == 0) WIDTH_RATIO
            else value
            invalidate()
        }

    var heightRatio = HEIGHT_RATIO
        private set(value) {
            field = if (value == 0) HEIGHT_RATIO
            else value
            invalidate()
        }

    /**
     * 阴影颜色
     */
    var dimmedColor = DIMMED_COLOR
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 阴影透明度
     */
    var dimmedAlpha = DIMMED_ALPHA
        set(value) {
            field = if (value > 225) DIMMED_ALPHA
            else value
            invalidate()
        }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        init()
    }

    private fun init() {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mCenterRectPath = Path()
        mCenterRectPaint.color = dimmedColor
        mCenterRectPaint.style = Paint.Style.FILL
        mCenterRectPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

        mDimmedPath = Path()
        mDimmedPaint.color = dimmedColor
        mDimmedPaint.style = Paint.Style.FILL_AND_STROKE
        mDimmedPaint.alpha = dimmedAlpha
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        context.obtainStyledAttributes(attrs, R.styleable.DimmedLayerView).run {
            widthRatio = getInt(R.styleable.DimmedLayerView_width_ratio, WIDTH_RATIO)
            heightRatio = getInt(R.styleable.DimmedLayerView_height_ratio, HEIGHT_RATIO)
            dimmedColor = getColor(R.styleable.DimmedLayerView_dimmed_color, DIMMED_COLOR)
            dimmedAlpha = getInt(R.styleable.DimmedLayerView_dimmed_alpha, DIMMED_ALPHA)
            recycle()
        }
    }

    //不用重写onMeasure，反正要铺满的

    //在onSizeChange中给path和paint赋值,其实也可以只在这里记录w，h然后在onDraw里给这些东西赋值
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDimmedPath.reset()
        mCenterRectPath.reset()
        //铺满全屏的阴影蒙板
        mDimmedPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
        /*开始计算中间抠图层*/
        //获得中心点坐标
        val centerX = (w / 2).toFloat()
        val centerY = (h / 2).toFloat()
        //中间抠图层和左右两边的边距 这里写死为view最小的边的 1/16
        val minSize = Math.min(w, h).toFloat()
        val space = minSize / 16 * 2
        //中间抠图层的宽高
        val width = minSize - space
        val height = width * widthRatio / heightRatio
        //获得中间抠图层的坐标
        val startX = centerX - width / 2
        val startY = centerY - height / 2
        val endX = centerX + width / 2
        val endY = centerY + height / 2
        mCenterRect = RectF(startX, startY, endX, endY)
        mCenterRectPath.addRect(mCenterRect, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawPath(mDimmedPath, mDimmedPaint)
        canvas.drawPath(mCenterRectPath, mCenterRectPaint)
    }
}