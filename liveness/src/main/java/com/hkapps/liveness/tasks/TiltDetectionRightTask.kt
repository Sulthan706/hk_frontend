package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class TiltDetectionRightTask : DetectionTask {
    companion object {
        private const val SHAKE_THRESHOLD = 15f
    }

    private var hasTiltToRight = false

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "TiltDetection"
    }

    override fun taskDescription(): String {
        return "miring ke kanan"
    }

    override fun start() {
        hasTiltToRight = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val roll = face.headEulerAngleZ

      if (roll < -SHAKE_THRESHOLD && !hasTiltToRight) {
            hasTiltToRight = true
        }

        return  hasTiltToRight
    }
}