package com.yoriz.yorizweatherview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * Created by yoriz
 * on 2019-05-07 13:29.
 */
class YoriZWeatherView : View {
    companion object {
        private const val DEFAULT_SIZE = 100
        private val DEFAULT_CLOUD_COLOR = Color.parseColor("#B0B0B0")
        private val DEFAULT_RAIN_COLOR = Color.parseColor("#80B9C5")
        private val DEFAULT_SUN_COLOR = Color.parseColor("#ffc20e")
        private const val DEFAULT_ALPHA = 0
        private const val DEFAULT_RAIN_SIZE = 10
        private const val DEFAULT_RAIN_COUNT = 4
        const val STYLE_CLOUD = 0
        const val STYLE_RAIN = 1
        const val STYLE_SUN = 2
        const val STYLE_SUN_CLOUD = 3
    }

    private var mCloudPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSunPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSunLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var w = 0
    private var h = 0

    private lateinit var mCloudPath: Path
    private lateinit var mSunPath: Path
    private lateinit var mRainPaths: List<Path>

    private lateinit var mRainRect: RectF

    private var mWeatherStyle = STYLE_CLOUD
    private var mCloudColor = DEFAULT_CLOUD_COLOR
    private var mSunColor = DEFAULT_SUN_COLOR
    private var mRainColor = DEFAULT_RAIN_COLOR
    private var mAlpha = DEFAULT_ALPHA

    var weatherStyle: Int
        get() = mWeatherStyle
        set(value) {
            mWeatherStyle = value
            invalidate()
        }
    var cloudColor: Int
        get() = mCloudColor
        set(value) {
            mCloudColor = value
            mCloudPaint.color = value
            invalidate()
        }
    var rainColor: Int
        get() = mRainColor
        set(value) {
            mRainColor = value
            mRainPaint.color = value
            invalidate()
        }
    var sunColor: Int
        get() = mSunColor
        set(value) {
            mSunColor = value
            mSunPaint.color = value
            mSunLinePaint.color = value
            invalidate()
        }
    var cloudAlpha: Int
        get() = mAlpha
        set(value) {
            mAlpha = value
            mRainPaint.alpha = value
            mCloudPaint.alpha = value
            invalidate()
        }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = widthSize
        var height = heightSize
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = DEFAULT_SIZE
            height = DEFAULT_SIZE
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = DEFAULT_SIZE
            height = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            width = widthSize
            height = DEFAULT_SIZE
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (mWeatherStyle) {
            STYLE_CLOUD -> {
                initCloudOrRainPath()
                canvas.drawPath(mCloudPath, mCloudPaint)
            }
            STYLE_RAIN -> {
                initCloudOrRainPath()
                canvas.drawPath(mCloudPath, mCloudPaint)
                for (i in 0 until DEFAULT_RAIN_COUNT) {
                    canvas.drawPath(mRainPaths[i], mRainPaint)
                }
            }
            STYLE_SUN -> {
                initSunOrCloudPath()
                canvas.drawPath(mSunPath, mSunPaint)
            }
            STYLE_SUN_CLOUD -> {
                initSunOrCloudPath()
                canvas.drawPath(mSunPath, mSunPaint)
                canvas.drawPath(mCloudPath, mCloudPaint)
            }
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        context.obtainStyledAttributes(attrs, R.styleable.YoriZWeatherView).run {
            weatherStyle = getInt(R.styleable.YoriZWeatherView_weather_style, STYLE_CLOUD)
            cloudColor = getColor(R.styleable.YoriZWeatherView_cloud_color, DEFAULT_CLOUD_COLOR)
            sunColor = getColor(R.styleable.YoriZWeatherView_sun_color, DEFAULT_SUN_COLOR)
            rainColor = getColor(R.styleable.YoriZWeatherView_rain_color, DEFAULT_RAIN_COLOR)
            cloudAlpha = getInt(R.styleable.YoriZWeatherView_cloud_alpha, DEFAULT_ALPHA)
            recycle()
        }
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mSunPaint.color = mSunColor
        mSunPaint.style = Paint.Style.FILL_AND_STROKE
        mSunPaint.setShadowLayer(10f, 3f, 3f, Color.DKGRAY)
        mSunPath = Path()

        mCloudPaint.color = mCloudColor
        mCloudPaint.style = Paint.Style.FILL
        mCloudPaint.setShadowLayer(10f, 3f, 3f, Color.DKGRAY)
        mCloudPath = Path()

        mRainPaint.color = mRainColor
        mRainPaint.strokeCap = Paint.Cap.ROUND
        mRainPaint.style = Paint.Style.FILL_AND_STROKE
        mRainPaint.strokeWidth = DEFAULT_RAIN_SIZE.toFloat()
        mRainRect = RectF()
        mRainPaths = ArrayList()
        for (i in 0 until DEFAULT_RAIN_COUNT) {
            (mRainPaths as ArrayList).add(Path())
        }
    }

    private fun initSunOrCloudPath() {
        mSunPath.reset()
        val size = min(w, h)
        val sunRadius = size / 4f
        val sunX = if (mWeatherStyle == STYLE_SUN_CLOUD) {
            size / 2f - (size / 2f) / 5f
        } else {
            size / 2f
        }
        val sunY = if (mWeatherStyle == STYLE_SUN_CLOUD) {
            size / 2f - (size / 2f) / 4f
        } else {
            size / 2f
        }
        mSunPath.fillType = Path.FillType.WINDING
        mSunPath.addCircle(sunX, sunY, sunRadius, Path.Direction.CW)

        mSunPaint.strokeWidth = sunRadius / 20
        val lineLength = sunRadius / 5
        val circleAndLineInterval = sunRadius / 10
        val lineStartRadius = sunRadius + circleAndLineInterval
        val lineEndRadius = lineStartRadius + lineLength
        val r = 360 / 12
        for (i in 1..12) {
            val a = Math.toRadians((r * i).toDouble())
            val lineStartX = sunX + lineStartRadius * Math.cos(a).toFloat()
            val lineStartY = sunY + lineStartRadius * Math.sin(a).toFloat()
            val lineEndX = sunX + lineEndRadius * Math.cos(a).toFloat()
            val lineEndY = sunY + lineEndRadius * Math.sin(a).toFloat()
            mSunPath.moveTo(lineStartX, lineStartY)
            mSunPath.lineTo(lineEndX, lineEndY)
        }

        if (mWeatherStyle == STYLE_SUN_CLOUD) {
            mCloudPath.reset()
            val cloudWidth = size.toFloat() * 0.9f
            val cloudBottomHeight = cloudWidth / 3f
            val cloudEndX = (size - cloudWidth) / 2 + cloudWidth
            val cloudEndY = size * 0.8f
            mCloudPath.fillType = Path.FillType.WINDING
            mCloudPath.addRoundRect(
                    RectF(cloudEndX - cloudWidth, cloudEndY - cloudBottomHeight, cloudEndX, cloudEndY),
                    cloudBottomHeight / 2, cloudBottomHeight / 2,
                    Path.Direction.CW
            )
            val cloudTopCenterY = cloudEndY - cloudBottomHeight
            val cloudTopCenterX = cloudEndX - cloudWidth * 0.4f
            mCloudPath.addCircle(cloudTopCenterX, cloudTopCenterY, cloudWidth / 4, Path.Direction.CW)
        }
    }

    private fun initCloudOrRainPath() {
        mCloudPath.reset()
        val size = min(w, h)
        val cloudWidth = size.toFloat() * 0.9f
        val cloudBottomHeight = cloudWidth / 3f
        val cloudEndX = (size - cloudWidth) / 2 + cloudWidth
        val cloudEndY = if (mWeatherStyle == STYLE_RAIN) {
            size / 2f + cloudBottomHeight / 3
        } else {
            size / 2f + cloudBottomHeight / 2
        }
        mCloudPath.fillType = Path.FillType.WINDING
        mCloudPath.addRoundRect(
                RectF(cloudEndX - cloudWidth, cloudEndY - cloudBottomHeight, cloudEndX, cloudEndY),
                cloudBottomHeight / 2, cloudBottomHeight / 2,
                Path.Direction.CW
        )
        val cloudTopCenterY = cloudEndY - cloudBottomHeight
        val cloudTopCenterX = cloudEndX - cloudWidth * 0.4f
        mCloudPath.addCircle(cloudTopCenterX, cloudTopCenterY, cloudWidth / 4, Path.Direction.CW)

        if (mWeatherStyle == STYLE_RAIN) {
            val calculateRect = RectF()
            mCloudPath.computeBounds(calculateRect, true)
            val top = calculateRect.bottom
            val bottom = size.toFloat()
            val interval = cloudWidth / DEFAULT_RAIN_COUNT
            val strokeWidth = interval / 5
            val rainHeight = (bottom - top - ((bottom - top) * 2 / 3)) / 2
            val startY = top + rainHeight
            val endY = bottom - rainHeight
            mRainPaint.strokeWidth = strokeWidth
            for (i in 0 until DEFAULT_RAIN_COUNT) {
                val path = mRainPaths[i]
                path.reset()
                //宽 * 总格数分之1+i*2
                val rainX = cloudEndX - cloudWidth + cloudWidth * (1 + i * 2).toFloat() / (DEFAULT_RAIN_COUNT * 2).toFloat()
                path.moveTo(rainX, startY)
                path.lineTo(rainX - 25, endY)
            }
        }
    }
}
