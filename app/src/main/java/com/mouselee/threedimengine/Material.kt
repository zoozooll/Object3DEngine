package com.mouselee.threedimengine

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLES30.*
import com.mouselee.threedimengine.sample.ShaderUtil
import java.nio.Buffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

open class Material {

    private var mProgram = 0 //自定义渲染管线程序id
//    private var muMVPMatrixHandle = 0 //总变换矩阵引用
//    private var maPositionHandle = 0 //顶点位置属性引用
//    private var maColorHandle = 0 //顶点颜色属性引用
    private val attributeLocationMap = HashMap<String, LocationValue>()
    private val uniformLocationMap = HashMap<String, LocationValue>()

    fun initShader(resources: Resources) {
        val mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", resources)
        val mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", resources)
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader)
        getAttributeLocations()
    }

    private fun getAttributeLocations() {
        val count = IntArray(1)
        glGetProgramiv(mProgram, GL_ACTIVE_ATTRIBUTES, count, 0)
        val sizeArray = IntArray(1)
        val typeArray = IntArray(1)
        for (i in 0 until count[0]) {
            val locationName = glGetActiveAttrib(mProgram, i, sizeArray, 0, typeArray, 0)
            val value = LocationValue().apply {
                name = locationName
                location = glGetAttribLocation(mProgram, locationName)
                size = sizeArray[0]
                type = typeArray[0]
            }
            attributeLocationMap[locationName] = value
        }
    }

    private fun getUniformLocations() {
        val count = IntArray(1)
        glGetProgramiv(mProgram, GL_ACTIVE_UNIFORMS, count, 0)
        val sizeArray = IntArray(1)
        val typeArray = IntArray(1)
        for (i in 0 until count[0]) {
            val locationName = glGetActiveUniform(mProgram, i, sizeArray, 0, typeArray, 0)
            val value = LocationValue().apply {
                name = locationName
                location = glGetUniformLocation(mProgram, locationName)
                size = sizeArray[0]
                type = typeArray[0]
            }
            uniformLocationMap[locationName] = value
        }
    }

    fun userProgram() {
        glUseProgram(mProgram)
    }

    fun setUniformValue(name: String, value: Any) {
        uniformLocationMap[name]?.run {
            when(type) {
                GL_FLOAT -> {
                    if (size == 1) {
                        glUniform1f(location, value as Float)
                    } else if (size > 1) {
                        glUniform1fv(location, (value as FloatArray).size, value, 0)
                    }
                }
                GL_FLOAT_VEC2 -> {
                    if (size == 1) {
                        glUniform2f(location, (value as FloatArray)[0], value[1])
                    } else if (size > 1) {
                        glUniform2fv(location, (value as FloatArray).size, value, 0)
                    }
                }
                GL_FLOAT_VEC3 -> {
                    if (size == 1) {
                        glUniform3f(location, (value as FloatArray)[0], value[1], value[2])
                    } else if (size > 1) {
                        glUniform2fv(location, (value as FloatArray).size, value, 0)
                    }
                }
                GL_FLOAT_VEC4 -> {
                    if (size == 1) {
                        glUniform4f(location, (value as FloatArray)[0], value[1], value[2], value[3])
                    } else if (size > 1) {
                        glUniform2fv(location, (value as FloatArray).size, value, 0)
                    }
                }
                GL_INT -> {
                    if (size == 1) {
                        glUniform1i(location, value as Int)
                    } else if (size > 1) {
                        glUniform1iv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_INT_VEC2 -> {
                    if (size == 1) {
                        glUniform2i(location, (value as IntArray)[0], value[1])
                    } else if (size > 1) {
                        glUniform2iv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_INT_VEC3 -> {
                    if (size == 1) {
                        glUniform3i(location, (value as IntArray)[0], value[1], value[2])
                    } else if (size > 1) {
                        glUniform3iv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_INT_VEC4 -> {
                    if (size == 1) {
                        glUniform4i(location, (value as IntArray)[0], value[1], value[2], value[3])
                    } else if (size > 1) {
                        glUniform4iv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_UNSIGNED_INT -> {
                    if (size == 1) {
                        glUniform1ui(location, value as Int)
                    } else if (size > 1) {
                        glUniform1uiv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_UNSIGNED_INT_VEC2 -> {
                    if (size == 1) {
                        glUniform2ui(location, (value as IntArray)[0], value[1])
                    } else if (size > 1) {
                        glUniform2uiv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_UNSIGNED_INT_VEC3 -> {
                    if (size == 1) {
                        glUniform3ui(location, (value as IntArray)[0], value[1], value[2])
                    } else if (size > 1) {
                        glUniform3uiv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_UNSIGNED_INT_VEC4 -> {
                    if (size == 1) {
                        glUniform4ui(location, (value as IntArray)[0], value[1], value[2], value[3])
                    } else if (size > 1) {
                        glUniform4uiv(location, (value as IntArray).size, value, 0)
                    }
                }
                GL_BOOL -> {

                }
                GL_BOOL_VEC2 -> {

                }
                GL_BOOL_VEC3 -> {

                }
                GL_BOOL_VEC4 -> {

                }
                GL_FLOAT_MAT2 -> {
                    glUniformMatrix2fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT3 -> {
                    glUniformMatrix3fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT4 -> {
                    glUniformMatrix4fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT2x3 -> {
                    glUniformMatrix2x3fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT2x4 -> {
                    glUniformMatrix2x4fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT3x2 -> {
                    glUniformMatrix3x2fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT3x4 -> {
                    glUniformMatrix3x4fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT4x2 -> {
                    glUniformMatrix4x2fv(location, size, false, value as FloatArray, 0)
                }
                GL_FLOAT_MAT4x3 -> {
                    glUniformMatrix4x3fv(location, size, false, value as FloatArray, 0)
                }
                else -> {}
            }

        }
    }

    fun setAttribValue(name: String, buffer: Buffer) {
        attributeLocationMap[name]?.run {
            var vectorSize = 0
            var vectorType = 0
            var typeSize = 0
            when(type) {
                GL_FLOAT -> {
                    vectorSize = size
                    vectorType = GL_FLOAT
                }
                GL_FLOAT_VEC2 -> {
                    vectorSize = 2 * size
                    vectorType = GL_FLOAT
                }
                GL_FLOAT_VEC3 -> {
                    vectorSize = 3 * size
                    vectorType = GL_FLOAT
                }
                GL_FLOAT_VEC4 -> {
                    vectorSize = 4 * size
                    vectorType = GL_FLOAT
                }
                GL_INT -> {
                    vectorSize = 1 * size
                    vectorType = GL_INT
                }
                GL_INT_VEC2 -> {
                    vectorSize = 2 * size
                    vectorType = GL_INT
                }
                GL_INT_VEC3 -> {
                    vectorSize = 3 * size
                    vectorType = GL_INT
                }
                GL_INT_VEC4 -> {
                    vectorSize = 4 * size
                    vectorType = GL_INT
                }
                GL_UNSIGNED_INT -> {
                    vectorSize = 1 * size
                    vectorType = GL_UNSIGNED_INT
                }
                GL_UNSIGNED_INT_VEC2 -> {
                    vectorSize = 2 * size
                    vectorType = GL_UNSIGNED_INT
                }
                GL_UNSIGNED_INT_VEC3 -> {
                    vectorSize = 3 * size
                    vectorType = GL_UNSIGNED_INT
                }
                GL_UNSIGNED_INT_VEC4 -> {
                    vectorSize = 4 * size
                    vectorType = GL_UNSIGNED_INT
                }
                GL_BOOL -> {

                }
                GL_BOOL_VEC2 -> {

                }
                GL_BOOL_VEC3 -> {

                }
                GL_BOOL_VEC4 -> {

                }
                else -> {}
            }
            glVertexAttribPointer(location, vectorSize, vectorType, false, vectorSize * typeSize, buffer)
        }
    }

    fun drawArrays(mode: Int, count: Int) {
        enableVertexAttribArray()
        glDrawArrays(mode, 0, count)
        disableVertexAttribArray()
    }

    fun drawElements(mode: Int, count: Int, indices: Buffer) {
        enableVertexAttribArray()
        val indexType = when(indices) {
            is ShortBuffer -> GL_UNSIGNED_SHORT
            is IntBuffer -> GL_UNSIGNED_INT
            else -> 0
        }
        glDrawElements(mode, count, indexType, indices)
        disableVertexAttribArray()
    }

    fun release() {
        glDeleteProgram(mProgram)
    }

    private fun enableVertexAttribArray() {
        attributeLocationMap.forEach { (t, u) ->
            glEnableVertexAttribArray(u.location)
        }
    }

    private fun disableVertexAttribArray() {
        attributeLocationMap.forEach { (t, u) ->
            glDisableVertexAttribArray(u.location)
        }
    }

    inner class LocationValue {
        var name = ""

        var location = -1

        var size = 0

        var type = 0
        override fun toString(): String {
            return "LocationValue(name='$name', location=$location, size=$size, type=${Integer.toHexString(type)})"
        }
    }
}