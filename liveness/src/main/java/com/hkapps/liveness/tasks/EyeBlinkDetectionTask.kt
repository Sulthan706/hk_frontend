package com.hkapps.liveness.tasks

import android.util.Log
import com.hkapps.liveness.utils.DetectionUtils
import com.google.mlkit.vision.face.Face
import kotlin.math.absoluteValue

class EyeBlinkDetectionTask : DetectionTask {

    private var timestamp: Long = 0L
    private var trackingId: Int = -1
    private var eyesOpen: Float = 0.9f
    private var eyesClose: Float = 0.1f
    private var leftEyeOpenProbabilityList: MutableList<Float> = arrayListOf()
    private var rightEyeOpenProbabilityList: MutableList<Float> = arrayListOf()

    private var sec = 0.1 * 1000000000
    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "EyesBlinkDetection"
    }

    override fun taskDescription(): String {
        return "kedipkan mata"
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        var leftEyeBlinked = false
        var rightEyeBlinked = false

        if ((this.timestamp - timestamp).absoluteValue > sec) {
            this.timestamp = timestamp
            Log.d(
                "EyesBlinkDetection",
                "Name: ${"name"} | id: ${face.trackingId} | time: $timestamp | LP: ${face.leftEyeOpenProbability} | RP: ${face.rightEyeOpenProbability}"
            )
            if (trackingId != face.trackingId) {
                trackingId = face.trackingId ?: -1
                Log.d(
                    "EyesBlinkDetectionTask",
                    " ===== Face is changed $trackingId ====="
                )
            } else {
//                AppLog.logToFile("EyesBlinkDetectionTask", "Face is same $trackingId")
                leftEyeOpenProbabilityList.add(face.leftEyeOpenProbability ?: 0f)
                if (leftEyeOpenProbabilityList.size > 3) {
                    if (leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 3] > eyesOpen && leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 2] < eyesClose && leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 1] > eyesOpen) {
                        leftEyeBlinked = true
                        Log.d(
                            "EyesBlinkDetectionTask",
                            "Name: ${"name"} | id: $trackingId | LP: ${leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 3]}, ${leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 2]}, ${leftEyeOpenProbabilityList[leftEyeOpenProbabilityList.size - 1]} ===== Left Eyes Blinked ====="
                        )
                    } else {
                        leftEyeBlinked = false
                    }
                }
                rightEyeOpenProbabilityList.add(face.rightEyeOpenProbability ?: 0f)
                if (rightEyeOpenProbabilityList.size > 3) {
                    if (rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 3] > eyesOpen && rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 2] < eyesClose && rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 1] > eyesOpen) {
                        rightEyeBlinked = true
                        Log.d(
                            "EyesBlinkDetectionTask",
                            "Name: ${"name"} | id: $trackingId | RP: ${rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 3]}, ${rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 2]}, ${rightEyeOpenProbabilityList[rightEyeOpenProbabilityList.size - 1]} ===== Right Eyes Blinked ====="
                        )
                    } else {
                        rightEyeBlinked = false
                    }
                }
            }
        }
        return leftEyeBlinked && rightEyeBlinked && DetectionUtils.isFacing(face)
    }
}