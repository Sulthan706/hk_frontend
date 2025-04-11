package com.hkapps.hygienekleen.features.facerecog.ui._new

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R

internal class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var transparentBackground: Paint? = null
    private var eraser: Paint? = null
    private var borderPaint: Paint? = null
    private var progressPaint: Paint? = null
    private var horizontalMargin = 0f
    private var verticalMargin = 0f
    private var currentBorderColor: Int = Color.WHITE
    private var currentProgressColor: Int = Color.GREEN
    private var progressBackgroundPaint: Paint? = null
    private var progressAnimator: ValueAnimator? = null
    private var isBackgroundProgressVisible: Boolean = false
    var progressValue: Float = 0f
    private var progressMargin = 10f

    private val radius: Float
        get() = ((measuredWidth - 2 * horizontalMargin).coerceAtMost(measuredHeight - 2 * verticalMargin) / 2f) * 1.25f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f - 250

        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

        transparentBackground?.let {
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), it)
        }

        eraser?.let {
            canvas.drawCircle(centerX, centerY, radius, it)
        }


        borderPaint?.let {
            canvas.drawCircle(centerX, centerY, radius, it)
        }

        val backgroundRectF = RectF(
            centerX - radius - progressMargin,
            centerY - radius - progressMargin,
            centerX + radius + progressMargin,
            centerY + radius + progressMargin
        )

        val progressRectF = RectF(
            centerX - radius - progressMargin,
            centerY - radius - progressMargin,
            centerX + radius + progressMargin,
            centerY + radius + progressMargin
        )

        if (isBackgroundProgressVisible) {
            progressBackgroundPaint?.let {
                canvas.drawArc(backgroundRectF, 0f, 360f, false, it)
            }
        }

        progressPaint?.let {
            canvas.drawArc(progressRectF, -90f, progressValue * 3.6f, false, it)
        }

        canvas.restoreToCount(layerId)
    }

    fun init(
        @ColorRes borderColorRes: Int = R.color.liveness_camerax_overlay_border,
        @ColorRes backgroundColorRes: Int = R.color.liveness_camerax_overlay_background,
        @DimenRes strokeWidthRes: Int = R.dimen.liveness_camerax_overlay_border_stroke,
        @DimenRes horizontalMarginRes: Int = R.dimen.liveness_camerax_overlay_horizontal_margin,
        @DimenRes verticalMarginRes: Int = R.dimen.liveness_camerax_overlay_vertical_margin,
        @DimenRes progressMarginRes: Int = R.dimen.liveness_camerax_overlay_progress_margin,
        @ColorRes progressColorRes: Int = R.color.gray
    ) {
        this.horizontalMargin = TypedValue().apply {
            resources.getValue(horizontalMarginRes, this, true)
        }.float
        this.verticalMargin = TypedValue().apply {
            resources.getValue(verticalMarginRes, this, true)
        }.float
        this.progressMargin = resources.getDimension(progressMarginRes)

        transparentBackground = Paint().apply {
            color = ContextCompat.getColor(context, backgroundColorRes)
            alpha = 100
            style = Paint.Style.FILL
        }

        eraser = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            style = Paint.Style.FILL
        }

        borderPaint = Paint().apply {
            color = ContextCompat.getColor(context, borderColorRes)
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = resources.getDimension(strokeWidthRes)
        }

        progressBackgroundPaint = Paint().apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = resources.getDimension(strokeWidthRes)
        }

        progressPaint = Paint().apply {
            color = ContextCompat.getColor(context, progressColorRes)
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = resources.getDimension(strokeWidthRes)
        }

        currentBorderColor = ContextCompat.getColor(context, borderColorRes)
        currentProgressColor = ContextCompat.getColor(context, progressColorRes)
    }


    fun setProgress(progress: Float) {
        val clampedProgress = progress.coerceIn(0f, 100f)

        progressAnimator = ValueAnimator.ofFloat(progressValue, clampedProgress).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { animator ->
                progressValue = animator.animatedValue as Float
                invalidate()
            }

            start()
        }
    }

    fun setBackgroundProgressVisibility(visible: Boolean) {
        isBackgroundProgressVisible = visible
        invalidate()
    }

    fun setBorderColor(color: Int) {
        currentBorderColor = color
        borderPaint?.color = color
        invalidate()
    }
}

