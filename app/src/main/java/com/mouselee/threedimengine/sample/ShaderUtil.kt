package com.mouselee.threedimengine.sample

import android.annotation.SuppressLint
import android.content.res.Resources
import android.opengl.GLES30.*
import android.util.Log
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

//加载顶点Shader与片元Shader的工具类
object ShaderUtil {
    //加载制定shader的方法
    fun loadShader(
        shaderType: Int,  //shader的类型  GLES30.GL_VERTEX_SHADER(顶点)   GLES30.GL_FRAGMENT_SHADER(片元)
        source: String? //shader的脚本字符串
    ): Int {
        //创建一个新shader
        var shader = glCreateShader(shaderType)
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源代码
            glShaderSource(shader, source)
            //编译shader
            glCompileShader(shader)
            //存放编译成功shader数量的数组
            val compiled = IntArray(1)
            //获取Shader的编译情况
            glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) { //若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader $shaderType:")
                Log.e("ES20_ERROR", glGetShaderInfoLog(shader))
                glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    //创建shader程序的方法
    fun createProgram(vertexSource: String?, fragmentSource: String?): Int {
        //加载顶点着色器
        val vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }

        //加载片元着色器
        val pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            return 0
        }

        //创建程序
        var program = glCreateProgram()
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")
            //向程序中加入片元着色器
            glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")
            //链接程序
            glLinkProgram(program)
            //存放链接成功program数量的数组
            val linkStatus = IntArray(1)
            //获取program的链接情况
            glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0)
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ")
                Log.e("ES20_ERROR", glGetProgramInfoLog(program))
                glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //检查每一步操作是否有错误的方法
    @SuppressLint("NewApi")
    fun checkGlError(op: String) {
        var error: Int
        while (glGetError().also { error = it } != GL_NO_ERROR) {
            Log.e("ES20_ERROR", "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }

    //从sh脚本中加载shader内容的方法
    fun loadFromAssetsFile(
        fname: String?,
        r: Resources
    ): String? {
        var result: String? = null
        try {
            val `in` = r.assets.open(fname!!)
            var ch = 0
            val baos = ByteArrayOutputStream()
            while (`in`.read().also { ch = it } != -1) {
                baos.write(ch)
            }
            val buff = baos.toByteArray()
            baos.close()
            `in`.close()
            result = String(buff, Charset.forName("UTF-8"))
            result = result.replace("\\r\\n".toRegex(), "\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}