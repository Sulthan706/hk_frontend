package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class ShakeDetectionRightTask : DetectionTask {

    companion object {
        private const val SHAKE_THRESHOLD = 20f
    }

    private var hasShakeToRight = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "ShakeDetectionRight"
    }

    override fun taskDescription(): String {
        return "Hadap kanan"
    }

    override fun start() {
        hasShakeToRight = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val yaw = face.headEulerAngleY

        if (yaw < -SHAKE_THRESHOLD && !hasShakeToRight) {
            hasShakeToRight = true
        }
        return hasShakeToRight
    }
}