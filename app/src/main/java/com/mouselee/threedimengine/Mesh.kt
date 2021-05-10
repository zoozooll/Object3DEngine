package com.mouselee.threedimengine

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

open class Mesh {
    lateinit var vertexBuffer: FloatBuffer
        private set

    var textureCoordBuffer: FloatBuffer? = null
        private set

    var normalBuffer: FloatBuffer? = null
        private set

    var colorBuffer: FloatBuffer? = null
        private set

    var indexBuffer: ShortBuffer? = null
        private set
    
    var vectorCount: Int = 0
    
    fun initVectorBuffer(vertices: FloatArray, count: Int) {
        vectorCount = count
        val byteBuffer = ByteBuffer.allocateDirect(vertices.size * Float.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder()) 
        vertexBuffer = byteBuffer.asFloatBuffer() 
        vertexBuffer.put(vertices)
        vertexBuffer.position(0) 
    }

    fun initTextureCoordBuffer(textureCoords: FloatArray) {
        val byteBuffer = ByteBuffer.allocateDirect(textureCoords.size * Float.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder())
        textureCoordBuffer = byteBuffer.asFloatBuffer()
        textureCoordBuffer!!.put(textureCoords)
        textureCoordBuffer!!.position(0)
    }

    fun initNormalBuffer(normals: FloatArray) {
        val byteBuffer = ByteBuffer.allocateDirect(normals.size * Float.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder())
        normalBuffer = byteBuffer.asFloatBuffer()
        normalBuffer!!.put(normals)
        normalBuffer!!.position(0)
    }

    fun initColorBuffer(colors: FloatArray) {
        val byteBuffer = ByteBuffer.allocateDirect(colors.size * Float.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder())
        colorBuffer = byteBuffer.asFloatBuffer()
        colorBuffer!!.put(colors)
        colorBuffer!!.position(0)
    }

    fun initIndexBuffer(colors: ShortArray) {
        val byteBuffer = ByteBuffer.allocateDirect(colors.size * Short.SIZE_BYTES)
        byteBuffer.order(ByteOrder.nativeOrder())
        indexBuffer = byteBuffer.asShortBuffer()
        indexBuffer!!.put(colors)
        indexBuffer!!.position(0)
    }

    fun release() {
        vertexBuffer.clear()
        textureCoordBuffer?.clear()
        normalBuffer?.clear()
        colorBuffer?.clear()
        indexBuffer?.clear()
        vectorCount = 0
    }
}