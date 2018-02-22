package com.saurabh.gitreposearchkotlin.customView

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ListView
import android.widget.RelativeLayout
import com.saurabh.gitreposearchkotlin.R

/**
 * Created by kiris on 2/21/2018.
 */
class RippleView:RelativeLayout {
    private val runnable = Runnable { invalidate() }
    private var WIDTH: Int = 0
    private var HEIGHT: Int = 0
    private var FRAME_RATE = 10
    private var DURATION = 60
    private var PAINT_ALPHA = 90
    private var canvasHandler: Handler? = null
    private var radiusMax = 0f
    private var animationRunning = false
    private var timer = 0
    private var timerEmpty = 0
    private var durationEmpty = -1
    private var x = -1f
    private var y = -1f
    private var zoomDuration: Int = 0
    private var zoomScale: Float = 0.toFloat()
    private var scaleAnimation: ScaleAnimation? = null
    private var hasToZoom: Boolean? = null
    private var isCentered: Boolean? = null
    private var rippleType: Int? = null
    private var paint: Paint? = null
    private var originBitmap: Bitmap? = null
    private var rippleColor: Int = 0
    private var ripplePadding: Int = 0
    private var gestureDetector: GestureDetector? = null

    constructor(context: Context): super(context)


    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        if (isInEditMode)
            return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView)
        rippleColor = typedArray.getColor(R.styleable.RippleView_rv_color, 0)
        rippleType = typedArray.getInt(R.styleable.RippleView_rv_type, 0)
        hasToZoom = typedArray.getBoolean(R.styleable.RippleView_rv_zoom, false)
        isCentered = typedArray.getBoolean(R.styleable.RippleView_rv_centered, false)
        DURATION = typedArray.getInteger(R.styleable.RippleView_rv_rippleDuration, DURATION)
        FRAME_RATE = typedArray.getInteger(R.styleable.RippleView_rv_framerate, FRAME_RATE)
        PAINT_ALPHA = typedArray.getInteger(R.styleable.RippleView_rv_alpha, PAINT_ALPHA)
        ripplePadding = typedArray.getDimensionPixelSize(R.styleable.RippleView_rv_ripplePadding, 0)
        canvasHandler = Handler()
        zoomScale = typedArray.getFloat(R.styleable.RippleView_rv_zoomScale, 1.03f)
        zoomDuration = typedArray.getInt(R.styleable.RippleView_rv_zoomDuration, 200)
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL
        paint!!.color = rippleColor
        paint!!.alpha = PAINT_ALPHA
        this.setWillNotDraw(false)

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(event: MotionEvent) {
                super.onLongPress(event)
                animateRipple(event)
                sendClickEvent(true)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

        this.isDrawingCacheEnabled = true
        this.isClickable = true
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (animationRunning) {
            if (DURATION <= timer * FRAME_RATE) {
                animationRunning = false
                timer = 0
                durationEmpty = -1
                timerEmpty = 0
                //                Saurabh-for marshmellow and above api exception
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
                    canvas.restore()
                invalidate()
                return
            } else
                canvasHandler!!.postDelayed(runnable, FRAME_RATE.toLong())

            if (timer == 0)
                canvas.save()


            canvas.drawCircle(x, y, radiusMax * (timer.toFloat() * FRAME_RATE / DURATION), paint!!)

            paint!!.color = resources.getColor(android.R.color.black)

            if (rippleType == 1 && originBitmap != null && timer.toFloat() * FRAME_RATE / DURATION > 0.4f) {
                if (durationEmpty == -1)
                    durationEmpty = DURATION - timer * FRAME_RATE

                timerEmpty++
                val tmpBitmap = getCircleBitmap((radiusMax * (timerEmpty.toFloat() * FRAME_RATE / durationEmpty)).toInt())
                canvas.drawBitmap(tmpBitmap, 0f, 0f, paint)
                tmpBitmap.recycle()
            }

            paint!!.color = rippleColor

            if (rippleType == 1) {
                if (timer.toFloat() * FRAME_RATE / DURATION > 0.6f)
                    paint!!.alpha = (PAINT_ALPHA - PAINT_ALPHA * (timerEmpty.toFloat() * FRAME_RATE / durationEmpty)).toInt()
                else
                    paint!!.alpha = PAINT_ALPHA
            } else
                paint!!.alpha = (PAINT_ALPHA - PAINT_ALPHA * (timer.toFloat() * FRAME_RATE / DURATION)).toInt()

            timer++
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        WIDTH = w
        HEIGHT = h

        scaleAnimation = ScaleAnimation(1.0f, zoomScale, 1.0f, zoomScale, (w / 2).toFloat(), (h / 2).toFloat())
        scaleAnimation!!.duration = zoomDuration.toLong()
        scaleAnimation!!.repeatMode = Animation.REVERSE
        scaleAnimation!!.repeatCount = 1
    }

    fun animateRipple(event: MotionEvent) {
        createAnimation(event.x, event.y)
    }

    fun animateRipple(x: Float, y: Float) {
        createAnimation(x, y)
    }

    private fun createAnimation(x: Float, y: Float) {
        if (!animationRunning) {
            if (hasToZoom!!)
                this.startAnimation(scaleAnimation)

            radiusMax = Math.max(WIDTH, HEIGHT).toFloat()

            if (rippleType != 2)
                radiusMax /= 2f

            radiusMax -= ripplePadding.toFloat()

            if (isCentered!! || rippleType == 1) {
                this.x = (measuredWidth / 2).toFloat()
                this.y = (measuredHeight / 2).toFloat()
            } else {
                this.x = x
                this.y = y
            }

            animationRunning = true

            if (rippleType == 1 && originBitmap == null)
                originBitmap = getDrawingCache(true)

            invalidate()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector!!.onTouchEvent(event)) {
            animateRipple(event)
            sendClickEvent(false)
        }
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        this.onTouchEvent(event)
        return super.onInterceptTouchEvent(event)
    }

    private fun sendClickEvent(isLongClick: Boolean?) {
        if (parent is ListView) {
            val position = (parent as ListView).getPositionForView(this)
            val id = (parent as ListView).getItemIdAtPosition(position)
            if (isLongClick!!) {
                if ((parent as ListView).onItemLongClickListener != null)
                    (parent as ListView).onItemLongClickListener.onItemLongClick(parent as ListView, this, position, id)
            } else {
                if ((parent as ListView).onItemClickListener != null)
                    (parent as ListView).onItemClickListener!!.onItemClick(parent as ListView, this, position, id)
            }
        }
    }

    private fun getCircleBitmap(radius: Int): Bitmap {
        val output = Bitmap.createBitmap(originBitmap!!.width, originBitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect((x - radius).toInt(), (y - radius).toInt(), (x + radius).toInt(), (y + radius).toInt())

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(x, y, radius.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(originBitmap!!, rect, rect, paint)

        return output
    }
}