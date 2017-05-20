package com.dev.baqari.pagerindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class CompleteIndicator : View {

    lateinit var mViewPager: ViewPager
    private var pageCount: Int = 0
    private var mCalculatedWidth: Int = 0
    lateinit var mUnfillPaint: Paint
    lateinit var mFillPaint: Paint
    lateinit var mClickPaint: Paint
    lateinit var mTextPaint: Paint
    private var mRadius: Int = 0
    private var mCurrentItemRadius: Int = 0
    private var mCurrentState: Int = 1
    private var mDelimition: Int = 0
    var onItemClickListener: OnItemClickListener? = null
    private var mShowNumbers: Boolean = true
    private var mShowLine: Boolean = true
    private var mLineSize: Int = 0
    private var mCurrentLineSize: Int = 50
    lateinit var circleCoordinates: HashMap<Int, Int>
    private var mItemClicked: Boolean = false
    private var mClickColorEnabled: Boolean = true

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {

        mUnfillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mUnfillPaint.color = Color.RED
        mFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFillPaint.color = Color.GREEN
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.typeface = Typeface.DEFAULT_BOLD
        mTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics)
        mTextPaint.color = Color.WHITE
        mClickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mClickPaint.color = Color.RED

        circleCoordinates = hashMapOf()

        val a = context.obtainStyledAttributes(attrs, R.styleable.CompleteIndicator, 0, 0)

        mRadius = a.getInt(R.styleable.CompleteIndicator_radius, 27)
        mCurrentItemRadius = a.getColor(R.styleable.CompleteIndicator_currentItemRadius, 33)
        val fillColor = a.getColor(R.styleable.CompleteIndicator_filledItemColor, Color.GREEN)
        val unFillColor = a.getColor(R.styleable.CompleteIndicator_unFilledItemColor, Color.RED)
        mShowNumbers = a.getBoolean(R.styleable.CompleteIndicator_showNumbers, true)
        mShowLine = a.getBoolean(R.styleable.CompleteIndicator_showLine, true)
        mLineSize = a.getInt(R.styleable.CompleteIndicator_lineSize, 10)
        mClickColorEnabled = a.getBoolean(R.styleable.CompleteIndicator_clickColorEnabled, true)
        var clickColor = a.getColor(R.styleable.CompleteIndicator_clickColor, Color.RED)

        mClickPaint.color = clickColor
        mUnfillPaint.color = unFillColor
        mFillPaint.color = fillColor

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        circleCoordinates.clear()

        var currentDelimition = 100f

        if (mShowLine) {
            var currentLineDelimition = 0f
            currentLineDelimition += ((mDelimition * mCurrentState) - 75).toFloat()
            canvas.drawRect(15f, mCurrentLineSize.toFloat(), currentLineDelimition, 60f + (mLineSize / 2).toFloat(), mFillPaint)
            canvas.drawRect(currentLineDelimition, mCurrentLineSize.toFloat(), (mCalculatedWidth - 25).toFloat(), 60f + (mLineSize / 2).toFloat(), mUnfillPaint)
        }
        
        (1..pageCount).forEach {
            if (mCurrentState > it) {
                circleCoordinates.put(it, currentDelimition.toInt())
                canvas.drawCircle(currentDelimition, mCurrentLineSize.toFloat() + 5, mRadius.toFloat(), mFillPaint)
            } else {
                circleCoordinates.put(it, currentDelimition.toInt())
                canvas.drawCircle(currentDelimition, mCurrentLineSize.toFloat() + 5, mRadius.toFloat(), mUnfillPaint)
            }

            if (mCurrentState == it) {
                if (mItemClicked && mClickColorEnabled) {
                    canvas.drawCircle(currentDelimition, mCurrentLineSize.toFloat() + 5, mCurrentItemRadius.toFloat(), mClickPaint)
                    Handler().postDelayed({
                        mItemClicked = false
                        invalidate()
                    }, 100)
                } else {
                    canvas.drawCircle(currentDelimition, mCurrentLineSize.toFloat() + 5, mCurrentItemRadius.toFloat(), mFillPaint)
                }
            }
            if (mShowNumbers)
                canvas.drawText(it.toString(), currentDelimition - 10, 64f, mTextPaint)

            currentDelimition += mDelimition
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mCalculatedWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mDelimition = mCalculatedWidth / pageCount
    }

    fun setViewPager(viewPager: ViewPager) {
        if (viewPager != null) {
            this.mViewPager = viewPager

            pageCount = mViewPager.adapter!!.count
            mCurrentState - viewPager.currentItem

            mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    mCurrentState = position + 1
                    invalidate()
                }

            })
        } else {
            throw NullPointerException("ViewPager must be initialized")
        }
    }

    var initialX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.rawX
            }
            MotionEvent.ACTION_UP -> {
                val diff = event.rawX - initialX
                if (diff < 10) {
                    val circle = getCircle(event.rawX.toInt())
                    if (circle != 0) {
                        onItemClickListener!!.item(circle)
                        mItemClicked = true
                    }
                }
            }
        }
        return true
    }

    fun getCircle(raw: Int): Int {
        var result = 0
        circleCoordinates.forEach {
            val diffFrom = raw - it.value
            val diffTo = it.value - raw
            if ((diffFrom in -20..10) || (diffTo in 10..40))
                result = it.key
        }
        return result
    }
}
