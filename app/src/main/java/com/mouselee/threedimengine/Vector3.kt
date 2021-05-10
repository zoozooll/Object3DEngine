package com.mouselee.threedimengine

class Vector3 {
    var x = 0F
    var y = 0F
    var z = 0F

    constructor() {
    }

    constructor(all: Float) {
        x = all
        y = all
        z = all
    }

    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun reset() {
        x = 0F
        y = 0F
        z = 0F
    }

    fun set(all: Float) {
        x = all
        y = all
        z = all
    }

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun set(vector3: Vector3) {
        x = vector3.x
        y = vector3.y
        z = vector3.z
    }
}