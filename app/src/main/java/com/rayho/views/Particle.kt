package com.rayho.views

/**
 * @Description:
 * @Author:         huangweihao
 * @CreateDate:     2020/9/29
 */
data class Particle(
    var x: Float, //粒子的x坐标
    var y: Float, //粒子的y坐标
    var radius: Float, //粒子的半径
    var offset: Float, //粒子的偏移量
    var speed: Float, //粒子的速度
    var maxOffset: Float, //粒子的最大移动距离
    var angle: Double //粒子的角度
)