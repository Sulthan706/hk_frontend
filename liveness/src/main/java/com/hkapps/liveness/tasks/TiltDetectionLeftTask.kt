package com.hkapps.liveness.tasks

import com.google.mlkit.vision.face.Face

class TiltDetectionLeftTask : DetectionTask{
    companion object {
        private const val SHAKE_THRESHOLD = 15f
    }

    private var hasTiltToLeft = false


    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "TiltDetection"
    }

    override fun taskDescription(): String {
        return "miring ke kiri"
    }

    override fun start() {
        hasTiltToLeft = false
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val roll = face.headEulerAngleZ

        if (roll > SHAKE_THRESHOLD && !hasTiltToLeft) {
            hasTiltToLeft = true
        }

        return hasTiltToLeft
    }
}