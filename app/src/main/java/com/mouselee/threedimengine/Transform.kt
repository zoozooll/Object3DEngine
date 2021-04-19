package com.mouselee.threedimengine

class Transform {
    var position = Vector3()

    var rotation = Vector3()

    var scale = Vector3(1F)

    var povit = Vector3()

    var parent: Transform? = null
}