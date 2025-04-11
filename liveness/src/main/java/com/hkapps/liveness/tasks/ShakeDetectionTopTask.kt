package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class ShakeDetectionTopTask : DetectionTask {

    companion object {
        private const val SHAKE_THRESHOLD = 5f
    }

    private var hasShakeToTop = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "ShakeDetectionRight"
    }

    override fun taskDescription(): String {
        return "Hadap atas"
    }

    override fun start() {
        hasShakeToTop = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val yaw = face.headEulerAngleX
        if (yaw < -SHAKE_THRESHOLD && !hasShakeToTop) {
            hasShakeToTop = true
        }
        return hasShakeToTop
    }
}