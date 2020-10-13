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
 * 具体实现参考https://juejin.im/post/6871049441546567688#comment
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
            //动画过程中 更新粒子的状态
            updateParticle()
            invalidate()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val time = measureTimeMillis {
            particleList.forEachIndexed { index, particle ->
                //改变粒子的透明度
                paint.alpha = ((1f - particle.offset / particle.maxOffset) * 225f).toInt()
                //画粒子
                canvas?.drawCircle(particle.x, particle.y, particleRadius, paint)
            }
        }
//        Log.d(this.javaClass.name,"绘制时间：${time}")
    }


    private fun updateParticle() {
        particleList.forEach {
            if (it.offset > it.maxOffset) {
                //粒子的偏移量大于最大移动距离时候 重置粒子的状态
                it.offset = 0f
                it.speed = Random.nextInt(6).toFloat() + 1.5f
                it.maxOffset = Random.nextInt(250).toFloat()
            }
            //扩散圆上粒子的坐标(粒子从内圆移动到扩散圆 内圆圆心的反方向移动)
            //具体坐标的计算参考链接文章
            it.x = (mWidth / 2 + cos(it.angle) * (it.offset + innerRadius)).toFloat()
            it.y = if (it.y > mHeight / 2) {
                (sin(it.angle) * (innerRadius + it.offset) + mHeight / 2).toFloat()
            } else {
                (mHeight / 2 - sin(it.angle) * (innerRadius + it.offset)).toFloat()
            }
            // (速度 = 距离 / 时间 因为在for循环中改变每个粒子的状态
            // 因此时间相当于不存在 因此粒子的移动距离等于offset += speed)
            it.offset += it.speed
        }
    }


    //在改方法中 初始化粒子的状态
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
        mWidth = w.toFloat()
        //添加圆的路径 但是不画圆
        path.addCircle(mWidth / 2, mHeight / 2, innerRadius, Path.Direction.CCW)
        pathMeasure.setPath(path, false)
        //初始化每个粒子的状态
        for (i in 0..particleNumber) {
            //按比例测量内圆路径上每一点的值
            pathMeasure.getPosTan(i / particleNumber.toFloat() * pathMeasure.length, pos, tan)
            //X值随机偏移
            val x = Random.nextInt(6) -3f + pos[0]
            //Y值随机偏移
            val y = Random.nextInt(6) - 3f + pos[1]
            //通过反余弦函数获取粒子的角度
            val angle = acos(((pos[0] - mWidth / 2) / innerRadius)).toDouble()
            //随机粒子的偏移量
            val offset = Random.nextInt(200)
            //随机粒子的速度
            val speed = Random.nextInt(2) + 0.5f
            //随机粒子的最大移动距离
            val maxOffset = Random.nextInt(250)
            //添加粒子
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