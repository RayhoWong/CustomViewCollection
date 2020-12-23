package com.rayho.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.rayho.customviewcollection.R
import kotlin.math.roundToInt

/**
 * @Description:
 * @Author:         huangweihao
 * @CreateDate:     2020/12/23
 */
class LoadingView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet),
    ValueAnimator.AnimatorUpdateListener {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rectF1 = RectF()
    val maxHeight = 120f
    val minHeight = 30f
    val mWidth = 15f
    val mSpilt = 18f
    var offset = 0f
    var animation: ValueAnimator

    var isGradient = false //是否渐变

    init {
        val typedArray = context?.obtainStyledAttributes(attributeSet, R.styleable.LoadingView)
        isGradient = typedArray?.getBoolean(R.styleable.LoadingView_isGradient, false)!!
        typedArray?.recycle()
        rectF1.set(0f, 0f, mWidth, maxHeight)
        paint.color = Color.WHITE
        animation = ValueAnimator.ofFloat(0f, minHeight)
        animation.duration = 350
        animation.repeatCount = -1
        animation.repeatMode = ValueAnimator.REVERSE
        animation.interpolator = LinearInterpolator()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until 5) {
            val startX = (i * (mSpilt + mWidth))
            if (i % 2 == 1) {
                rectF1.set(startX, offset, startX + mWidth, maxHeight - offset)
                if (isGradient) {
                    val shader = getShader(startX, offset, startX + mWidth, maxHeight - offset)
                    paint.shader = shader
                }
            } else {
                rectF1.set(
                    startX,
                    minHeight - offset,
                    startX + mWidth,
                    maxHeight - minHeight + offset
                )
                if (isGradient) {
                    val shader = getShader(
                        startX,
                        minHeight - offset,
                        startX + mWidth,
                        maxHeight - minHeight + offset
                    )
                    paint.shader = shader
                }
            }
            canvas?.drawRoundRect(rectF1, 10f, 10f, paint)
        }
        rectF1.set(0f, 0f, mWidth, maxHeight)
    }


    private fun getShader(x0: Float, y0: Float, x1: Float, y1: Float): Shader {
        val shader = LinearGradient(
            x0, y0, x1, y1, Color.parseColor("#ff843a"),
            Color.parseColor("#ffebde"), Shader.TileMode.CLAMP
        )
        return shader
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((5 * mWidth + 4 * mSpilt).roundToInt(), maxHeight.roundToInt())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation.removeUpdateListener(this)
        animation.cancel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation.addUpdateListener(this)
        animation.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        offset = animation?.animatedValue as Float
        invalidate()
    }
}