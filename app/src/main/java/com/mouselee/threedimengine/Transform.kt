package com.mouselee.threedimengine

class Transform {
    val position = Vector3()

    val rotation = Vector3()

    val scale = Vector3(1F)

    val pivot = Vector3()

    val parent: Transform? = null

    private val modelMatrix = FloatArray(16)

    val worldMatrix = FloatArray(16)

    fun updateTransform() {

    }

    private fun calculateModelMatrix() {

    }
}