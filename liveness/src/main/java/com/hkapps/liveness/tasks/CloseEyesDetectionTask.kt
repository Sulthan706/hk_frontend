package com.hkapps.liveness.tasks

import android.util.Log
import com.hkapps.liveness.utils.DetectionUtils
import com.google.mlkit.vision.face.Face
import kotlin.math.absoluteValue

class CloseEyesDetectionTask : DetectionTask {

    private var timestamp: Long = 0L
    private var trackingId: Int = -1
    private var eyesClose: Float = 0.5f
    private var leftEyeCloseProbabilityList: MutableList<Float> = arrayListOf()
    private var rightEyeCloseProbabilityList: MutableList<Float> = arrayListOf()

    private var sec = 0.1 * 1_000_000_000
    private var framesEyeClosedThreshold = 5

    override var isTaskCompleted: Boolean = false

    override fun taskName(): String {
        return "EyesClosedDetection"
    }

    override fun taskDescription(): String {
        return "Pejamkan mata"
    }

    override fun process(face: Face, timestamp: Long): Boolean {
        var isLeftEyeClosed = false
        var isRightEyeClosed = false

        if ((this.timestamp - timestamp).absoluteValue > sec) {
            this.timestamp = timestamp
            Log.d(
                "EyesClosedDetection",
                "Name: ${"name"} | id: ${face.trackingId} | time: $timestamp | LP: ${face.leftEyeOpenProbability} | RP: ${face.rightEyeOpenProbability}"
            )

            if (trackingId != face.trackingId) {
                trackingId = face.trackingId ?: -1
                Log.d(
                    "EyesClosedDetectionTask",
                    " ===== Face is changed $trackingId ====="
                )
            } else {

                leftEyeCloseProbabilityList.add(face.leftEyeOpenProbability ?: 0f)
                if (leftEyeCloseProbabilityList.size >= framesEyeClosedThreshold) {

                    isLeftEyeClosed = leftEyeCloseProbabilityList.takeLast(framesEyeClosedThreshold).all { it < eyesClose }
                    if (isLeftEyeClosed) {
                        Log.d(
                            "EyesClosedDetectionTask",
                            "Name: ${"name"} | id: $trackingId | LP: ${leftEyeCloseProbabilityList.takeLast(framesEyeClosedThreshold)} ===== Left Eye Closed ====="
                        )
                    }
                }


                rightEyeCloseProbabilityList.add(face.rightEyeOpenProbability ?: 0f)
                if (rightEyeCloseProbabilityList.size >= framesEyeClosedThreshold) {

                    isRightEyeClosed = rightEyeCloseProbabilityList.takeLast(framesEyeClosedThreshold).all { it < eyesClose }
                    if (isRightEyeClosed) {
                        Log.d(
                            "EyesClosedDetectionTask",
                            "Name: ${"name"} | id: $trackingId | RP: ${rightEyeCloseProbabilityList.takeLast(framesEyeClosedThreshold)} ===== Right Eye Closed ====="
                        )
                    }
                }

                if (leftEyeCloseProbabilityList.size > 10) {
                    leftEyeCloseProbabilityList.removeAt(0)
                }
                if (rightEyeCloseProbabilityList.size > 10) {
                    rightEyeCloseProbabilityList.removeAt(0)
                }
            }
        }

        isTaskCompleted = isLeftEyeClosed && isRightEyeClosed
        return isTaskCompleted && DetectionUtils.isFacing(face)
    }
}
