package com.hkapps.liveness.detection

import android.util.Log
import com.hkapps.liveness.tasks.*
import com.hkapps.liveness.utils.DetectionUtils
import com.google.mlkit.vision.face.Face
import java.util.Deque
import java.util.LinkedList

class LivenessDetector() {

    companion object {
        private const val FACE_CACHE_SIZE = 5
        private const val NO_ERROR = -1
        const val ERROR_NO_FACE = 0
        const val ERROR_MULTI_FACES = 1
        const val ERROR_OUT_OF_DETECTION_RECT = 2
    }

    val listTaskDetection = mutableListOf<String>(
        "Please squarely facing the camera",
//        "Pejamkan mata",
//        "Kedip",
//        "Buka mulut",
//        "Senyum",
//        "Hadap kanan",
//        "Hadap kiri",
//        "Miring kanan",
//        "Miring kiri",
    )

    private val taskMap = mapOf(
        "Please squarely facing the camera" to FacingDetectionTask(),
        "Pejamkan mata" to CloseEyesDetectionTask(),
        "Kedip" to EyeBlinkDetectionTask(),
        "Buka mulut" to OpenMouthDetectionTask(),
        "Senyum" to SmileDetectionTask(),
        "Hadap kanan" to ShakeDetectionRightTask(),
        "Hadap kiri" to ShakeDetectionLeftTask(),
        "Miring kanan" to TiltDetectionLeftTask(),
        "Miring kiri" to TiltDetectionRightTask()
    )

    private lateinit var tasks: MutableList<DetectionTask>
    private var taskIndex = 0
    private var lastTaskIndex = -1
    private var currentErrorState = NO_ERROR
    private val lastFaces: Deque<Face> = LinkedList()
    private var listener: Listener? = null
    lateinit var randomizedTasks: MutableList<String>

//    init {
//        resetTasks()
//    }

    private fun generateRandomTasks(): MutableList<String> {
        randomizedTasks = listTaskDetection
            .filter { it != "Please squarely facing the camera" }
            .shuffled()
            .take(3)
            .toMutableList()

        randomizedTasks.add(0,"Please squarely facing the camera")
        Log.d("TESTEDKK","$randomizedTasks $listTaskDetection")
        return randomizedTasks
    }

    fun resetTasks() {
        randomizedTasks = generateRandomTasks()
        tasks = randomizedTasks.mapNotNull { taskMap[it] }.toMutableList()
    }

    fun process(faces: List<Face>?, detectionSize: Int, timestamp: Long) {
        val task = tasks.getOrNull(taskIndex) ?: return
        Log.d("TESTED","$task ")
        if (taskIndex != lastTaskIndex) {
            lastTaskIndex = taskIndex
            task.start()
            listener?.onTaskStarted(task)
        }

        val face = filter(task, faces, detectionSize) ?: return
        if (task.process(face, timestamp)) {
            task.isTaskCompleted = true
            listener?.onTaskCompleted(task, taskIndex == tasks.lastIndex)
            taskIndex++
        }
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun getTaskSize(): Int {
        return this.tasks.size
    }

    fun getTasks(): List<DetectionTask> {
        return this.tasks
    }

    private fun reset() {
        taskIndex = 0
        lastTaskIndex = -1
        lastFaces.clear()
        resetTasks()
        tasks.forEach { it.isTaskCompleted = false }
    }

    private fun filter(task: DetectionTask, faces: List<Face>?, detectionSize: Int): Face? {
        if (faces != null && faces.size > 1) {
            changeErrorState(task, ERROR_MULTI_FACES)
            reset()
            return null
        }

        if (faces.isNullOrEmpty() && lastFaces.isEmpty()) {
            changeErrorState(task, ERROR_NO_FACE)
            reset()
            return null
        }

        val face = faces?.firstOrNull() ?: lastFaces.pollFirst()
        if (!DetectionUtils.isFaceInDetectionRect(face, detectionSize)) {
            changeErrorState(task, ERROR_OUT_OF_DETECTION_RECT)
            reset()
            return null
        }

        if (!faces.isNullOrEmpty()) {
            lastFaces.offerFirst(face)
            if (lastFaces.size > FACE_CACHE_SIZE) {
                lastFaces.pollLast()
            }
        }
        changeErrorState(task, NO_ERROR)
        return face
    }

    private fun changeErrorState(task: DetectionTask, newErrorState: Int) {
        if (newErrorState != currentErrorState) {
            currentErrorState = newErrorState
            if (currentErrorState != NO_ERROR) {
                listener?.onTaskFailed(task, currentErrorState)
            }
        }
    }

    interface Listener {
        fun onTaskStarted(task: DetectionTask)
        fun onTaskCompleted(task: DetectionTask, isLastTask: Boolean)
        fun onTaskFailed(task: DetectionTask, code: Int)
    }
}
