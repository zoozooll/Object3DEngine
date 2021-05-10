package com.mouselee.threedimengine.sample

import android.annotation.SuppressLint
import android.opengl.GLES30.*
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(mv: View) {
    private var mProgram = 0 //自定义渲染管线程序id
    private var muMVPMatrixHandle = 0 //总变换矩阵引用
    private var maPositionHandle = 0 //顶点位置属性引用
    private var maColorHandle = 0 //顶点颜色属性引用
    private var mVertexShader //顶点着色器代码脚本
            : String? = null
    private var mFragmentShader //片元着色器代码脚本
            : String? = null
    private var mVertexBuffer //顶点坐标数据缓冲
            : FloatBuffer? = null
    private var mColorBuffer //顶点着色数据缓冲
            : FloatBuffer? = null
    private var vCount = 0
    var xAngle = 0f //绕x轴旋转的角度
    private fun initVertexData() //初始化顶点数据的方法
    {
        //顶点坐标数据的初始化
        vCount = 3
        val UNIT_SIZE = 0.2f
        val vertices = floatArrayOf(
            -4 * UNIT_SIZE, 0f, 0f, 0f, -4 * UNIT_SIZE, 0f,
            4 * UNIT_SIZE, 0f, 0f
        )
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder()) //设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer() //转换为浮点(Float)型缓冲
        mVertexBuffer!!.put(vertices) //在缓冲区内写入数据
        mVertexBuffer!!.position(0) //设置缓冲区起始位置
        val colors = floatArrayOf(1f, 1f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f)
        val cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder()) //设置字节顺序为本地操作系统顺序
        mColorBuffer = cbb.asFloatBuffer() //转换为浮点(Float)型缓冲
        mColorBuffer!!.put(colors) //在缓冲区内写入数据
        mColorBuffer!!.position(0) //设置缓冲区起始位置
    }

    //初始化着色器的方法
    @SuppressLint("NewApi")
    private fun initShader(mv: View) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", mv.resources)
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", mv.resources)
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader)
        //获取程序中顶点位置属性引用  
        maPositionHandle = glGetAttribLocation(mProgram, "aPosition")
        //获取程序中顶点颜色属性引用
        maColorHandle = glGetAttribLocation(mProgram, "aColor")
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix")
    }

    fun drawSelf() {
        //指定使用某套shader程序
        glUseProgram(mProgram)
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0f, 0f, 1f, 0f)
        //设置沿Z轴正向位移1
        Matrix.translateM(mMMatrix, 0, 0f, 0f, 1f)
        //设置绕x轴旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1f, 0f, 0f)
        //将变换矩阵传入渲染管线
        glUniformMatrix4fv(
            muMVPMatrixHandle,
            1,
            false,
            getFianlMatrix(mMMatrix),
            0
        )
        //将顶点位置数据传送进渲染管线
        glVertexAttribPointer(
            maPositionHandle,
            3,
            GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        )
        //将顶点颜色数据传送进渲染管线
        glVertexAttribPointer(
            maColorHandle,
            4,
            GL_FLOAT,
            false,
            4 * 4,
            mColorBuffer
        )
        glEnableVertexAttribArray(maPositionHandle) //启用顶点位置数据
        glEnableVertexAttribArray(maColorHandle) //启用顶点着色数据
        //绘制三角形
        glDrawArrays(GL_TRIANGLES, 0, vCount)
    }

    companion object {
        var mProjMatrix = FloatArray(16) //4x4 投影矩阵
        var mVMatrix = FloatArray(16) //摄像机位置朝向的参数矩阵
        var mMVPMatrix //最后起作用的总变换矩阵
                : FloatArray? = null
        var mMMatrix = FloatArray(16) //具体物体的移动旋转矩阵，包括旋转、平移、缩放
        fun getFianlMatrix(spec: FloatArray?): FloatArray {
            mMVPMatrix = FloatArray(16)
            Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0)
            Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0)
            return mMVPMatrix!!
        }
    }

    init {
        //调用初始化顶点数据的initVertexData方法
        initVertexData()
        //调用初始化着色器的intShader方法
        initShader(mv)
    }
}