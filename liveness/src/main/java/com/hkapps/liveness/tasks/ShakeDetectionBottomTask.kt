package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class ShakeDetectionBottomTask : DetectionTask{

    companion object {
        private const val SHAKE_THRESHOLD = 5f
    }

    private var hasShakeToBottom = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "ShakeDetectionRight"
    }

    override fun taskDescription(): String {
        return "Hadap bawah"
    }

    override fun start() {
        hasShakeToBottom = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val yaw = face.headEulerAngleX
        if (yaw < -SHAKE_THRESHOLD && !hasShakeToBottom) {
            hasShakeToBottom = true
        }
        return hasShakeToBottom
    }

}
