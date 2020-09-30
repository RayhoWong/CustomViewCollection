package com.rayho.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * @Description:
 * @Author:         huangweihao
 * @CreateDate:     2020/9/27
 */
class NeteasyParticleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mHeight = 0f
    private var mWidth = 0f

    private val particleNumber = 2000 //粒子数量
    private val particleRadius = 2.2f //粒子半径
    private val innerRadius = 268f //初始圆半径
    private val particleList by lazy {
        mutableListOf<Particle>()
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var path = Path()
    private val animator = ValueAnimator.ofFloat(0f, 1f)

    private val pathMeasure = PathMeasure()
    private val pos = FloatArray(2) //初始圆上某点的坐标x,y
    private val tan = FloatArray(2) // 初始圆上某点的正切值


    init {
        paint.color = Color.WHITE
        animator.repeatCount = -1
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateParticle()
            invalidate()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val time = measureTimeMillis {
            particleList.forEachIndexed { index, particle ->
                paint.alpha = ((1f - particle.offset / particle.maxOffset) * 225f).toInt()
                canvas?.drawCircle(particle.x, particle.y, particleRadius, paint)
            }
        }
//        Log.d(this.javaClass.name,"绘制时间：${time}")
    }


    private fun updateParticle() {
        particleList.forEach {
            if (it.offset > it.maxOffset) {
                it.offset = 0f
                it.speed = Random.nextInt(6).toFloat() + 1.5f
                it.maxOffset = Random.nextInt(250).toFloat()
            }
            it.x = (mWidth / 2 + cos(it.angle) * (it.offset + innerRadius)).toFloat()
            it.y = if (it.y > mHeight / 2) {
                (sin(it.angle) * (innerRadius + it.offset) + mHeight / 2).toFloat()
            } else {
                (mHeight / 2 - sin(it.angle) * (innerRadius + it.offset)).toFloat()
            }
            it.offset += it.speed
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
        mWidth = w.toFloat()
        path.addCircle(mWidth / 2, mHeight / 2, innerRadius, Path.Direction.CCW)
        pathMeasure.setPath(path, false)
        for (i in 0..particleNumber) {
            pathMeasure.getPosTan(i / particleNumber.toFloat() * pathMeasure.length, pos, tan)
            val x = Random.nextInt(6) -3f + pos[0]
            val y = Random.nextInt(6) - 3f + pos[1]
            val angle = acos(((pos[0] - mWidth / 2) / innerRadius)).toDouble()
            val offset = Random.nextInt(200)
            val speed = Random.nextInt(2) + 0.5f
            val maxOffset = Random.nextInt(250)
            particleList.add(
                Particle(
                    x,
                    y,
                    particleRadius,
                    offset.toFloat(),
                    speed,
                    maxOffset.toFloat(),
                    angle
                )
            )
        }
        animator.start()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

}