package com.mouselee.threedimengine.sample

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyTDView : GLSurfaceView, GLSurfaceView.Renderer {
    val ANGLE_SPAN = 0.375f
    var rthread: RotateThread? = null
    var tle: Triangle? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(3)
        setRenderer(this)
        this.renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onDrawFrame(gl: GL10) {
        //清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        //绘制三角形对象
        tle?.drawSelf()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        //设置视窗大小及位置
        GLES30.glViewport(0, 0, width, height)
        //计算GLSurfaceView的宽高比
        val ratio = width.toFloat() / height
        //调用此方法计算产生透视投影矩阵
        Matrix.frustumM(
            Triangle.Companion.mProjMatrix,
            0,
            -ratio,
            ratio,
            -1f,
            1f,
            1f,
            10f
        )
        //调用此方法产生摄像机9参数位置矩阵
        Matrix.setLookAtM(
            Triangle.Companion.mVMatrix,
            0,
            0f,
            0f,
            3f,
            0f,
            0f,
            0f,
            0f,
            1.0f,
            0.0f
        )
    }

    override fun onSurfaceCreated(
        gl: GL10,
        config: EGLConfig
    ) {
        //设置屏幕背景色RGBA
        GLES30.glClearColor(0f, 0f, 0f, 1.0f)
        //创建三角形对对象
        tle = Triangle(this@MyTDView)
        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        rthread = RotateThread()
        rthread!!.start()
    }

    inner class RotateThread : Thread() //自定义的内部类线程
    {
        var flag = true
        override fun run() //重写的run方法
        {
            while (flag) {
                tle!!.xAngle = tle!!.xAngle + ANGLE_SPAN
                try {
                    sleep(20)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}