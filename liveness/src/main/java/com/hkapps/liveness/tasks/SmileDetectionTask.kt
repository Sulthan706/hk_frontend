package com.hkapps.liveness.tasks

import com.hkapps.liveness.utils.DetectionUtils
import com.google.mlkit.vision.face.Face

class SmileDetectionTask : DetectionTask {

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "SmileDetection"
    }

    override fun taskDescription(): String {
        return "Senyum"
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        val isSmile = (face.smilingProbability ?: 0f) > 0.8f
        return isSmile && DetectionUtils.isFacing(face)
    }
}