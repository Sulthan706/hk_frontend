package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

interface DetectionTask {

    var isTaskCompleted: Boolean

    fun taskName(): String {
        return "Detection"
    }

    fun taskDescription(): String {
        return ""
    }

    fun start() {}


    fun process(face: Face, timestamp: Long): Boolean
}