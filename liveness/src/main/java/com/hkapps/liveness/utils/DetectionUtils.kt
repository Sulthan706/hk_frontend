package com.hkapps.liveness.utils

import android.graphics.PointF
import android.util.Log
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

object DetectionUtils {

    fun isFacing(face: Face): Boolean {
        return face.headEulerAngleZ < 7.78f && face.headEulerAngleZ > -7.78f
                && face.headEulerAngleY < 11.8f && face.headEulerAngleY > -11.8f
                && face.headEulerAngleX < 19.8f && face.headEulerAngleX > -19.8f
    }

    fun isMouthOpened(face: Face): Boolean {
        val left = face.getLandmark(FaceLandmark.MOUTH_LEFT)?.position ?: return false
        val right = face.getLandmark(FaceLandmark.MOUTH_RIGHT)?.position ?: return false
        val bottom = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position ?: return false

        val a2 = lengthSquare(right, bottom)
        val b2 = lengthSquare(left, bottom)
        val c2 = lengthSquare(left, right)

        val a = sqrt(a2)
        val b = sqrt(b2)

        val gamma = acos((a2 + b2 - c2) / (2 * a * b))

        val gammaDeg = gamma * 180 / Math.PI
        return gammaDeg < 105f
    }

    private fun lengthSquare(a: PointF, b: PointF): Float {
        val x = a.x - b.x
        val y = a.y - b.y
        return x * x + y * y
    }

    fun isFaceInDetectionRect(face: Face, detectionSize: Int): Boolean {
        val centerX = detectionSize / 2f
        val centerY = detectionSize / 2f - 250
        val radius = (detectionSize / 2f) * 1.4f

        val fRect = face.boundingBox
        val fx = fRect.centerX().toFloat()
        val fy = fRect.centerY().toFloat()

        val faceCenterDistance = sqrt(
            ((fx - centerX).toDouble().pow(2.0) + (fy - centerY).toDouble().pow(2.0))
        )
        if (faceCenterDistance > radius) {
            Log.d("isFaceInDetectionRect", "face center point is out of the circle: ($fx, $fy)")
            return false
        }

        val fw = fRect.width().toFloat()
        val fh = fRect.height().toFloat()

        val minFaceSize = radius * 0.2f
        val maxFaceSize = radius * 0.8f
        if (fw < minFaceSize || fw > maxFaceSize || fh < minFaceSize || fh > maxFaceSize) {
            Log.d("isFaceInDetectionRect", "unexpected face size: $fw x $fh")
            return false
        }

        return true
    }

}