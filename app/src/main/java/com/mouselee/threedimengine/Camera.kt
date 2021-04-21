package com.mouselee.threedimengine

import android.opengl.Matrix

open class Camera : Object3D() {
    private var projectionMatrix = FloatArray(16)

    private var viewMatrix = FloatArray(16)

    private var mVPMatrix = FloatArray(16)

    private var cameraTransformMatrix = FloatArray(16)

    init {
        Matrix.setLookAtM(viewMatrix, 0, 0F, 0f, 0F,
            0F, 0F, -1F, 0F, 1F, 0F)
    }

    fun frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
    }

    fun orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far)
    }

    fun perspective(fovy: Float, aspect: Float, zNear: Float, zFar: Float) {
        Matrix.perspectiveM(projectionMatrix, 0, fovy, aspect, zNear, zFar)
    }

    fun getMVPMatrix(modelMatrix: FloatArray): FloatArray {
        mVPMatrix = FloatArray(16)
        Matrix.multiplyMM(mVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, cameraTransformMatrix, 0, mVPMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, mVPMatrix, 0)
        return mVPMatrix!!
    }
}