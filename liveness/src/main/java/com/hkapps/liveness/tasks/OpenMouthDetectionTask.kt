package com.hkapps.liveness.tasks

import com.hkapps.liveness.utils.DetectionUtils
import com.google.mlkit.vision.face.Face

class OpenMouthDetectionTask : DetectionTask {

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "MouthOpenDetection"
    }

    override fun taskDescription(): String {
        return "Buka mulut"
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        return DetectionUtils.isFacing(face) && DetectionUtils.isMouthOpened(face)
    }
}