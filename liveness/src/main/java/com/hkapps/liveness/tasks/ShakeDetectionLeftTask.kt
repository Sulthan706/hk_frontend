package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class ShakeDetectionLeftTask : DetectionTask {

    companion object {
        private const val SHAKE_THRESHOLD = 20f
    }

    private var hasShakeToLeft = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "ShakeDetection"
    }

    override fun taskDescription(): String {
        return "Hadap kiri"
    }

    override fun start() {
        hasShakeToLeft = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val yaw = face.headEulerAngleY
        if (yaw > SHAKE_THRESHOLD && !hasShakeToLeft) {
            hasShakeToLeft = true
        }
        return hasShakeToLeft
    }
}