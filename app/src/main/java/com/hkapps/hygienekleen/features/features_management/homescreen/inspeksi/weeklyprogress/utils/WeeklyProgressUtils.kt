package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeeklyProgressUtils {

    fun addTimestamp(bitmap: Bitmap, backgroundColor: Int, textColor: Int): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = textColor
            paint.textSize = 400f
            paint.isAntiAlias = true
            paint.typeface = Typeface.create("monospace", Typeface.BOLD)

            val strokePaint = Paint()
            strokePaint.color = Color.BLACK
            strokePaint.textSize = 400f
            strokePaint.isAntiAlias = true
            strokePaint.typeface = Typeface.create("monospace", Typeface.BOLD)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 20f

            val dateText = timestamp.split(" ")[0] // Get the date part
            val timeText = timestamp.split(" ")[1] // Get the time part

            val dateBounds = Rect()
            val timeBounds = Rect()
            paint.getTextBounds(dateText, 0, dateText.length, dateBounds)
            paint.getTextBounds(timeText, 0, timeText.length, timeBounds)

            val xDate = 90f
            val xTime = 90f
            val yDate = bitmap.height - 20f - dateBounds.height()
            val yTime = yDate - 20f - timeBounds.height()

            // Calculate background rectangles for date and time
            val dateBackgroundRect =
                RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect =
                RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color =
                backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
            canvas.drawRect(dateBackgroundRect, backgroundPaint)
            canvas.drawRect(timeBackgroundRect, backgroundPaint)

            // Draw the date and time text with stroke
            canvas.drawText(dateText, xDate, yDate, strokePaint)
            canvas.drawText(dateText, xDate, yDate, paint)
            canvas.drawText(timeText, xTime, yTime, strokePaint)
            canvas.drawText(timeText, xTime, yTime, paint)

            return bitmap
        } else {
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = textColor
            paint.textSize = 400f
            paint.isAntiAlias = true
            paint.typeface = Typeface.create("monospace", Typeface.BOLD)

            val strokePaint = Paint()
            strokePaint.color = Color.BLACK
            strokePaint.textSize = 400f
            strokePaint.isAntiAlias = true
            strokePaint.typeface = Typeface.create("monospace", Typeface.BOLD)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 20f

            val dateText = timestamp.split(" ")[0] // Get the date part
            val timeText = timestamp.split(" ")[1] // Get the time part

            val dateBounds = Rect()
            val timeBounds = Rect()
            paint.getTextBounds(dateText, 0, dateText.length, dateBounds)
            paint.getTextBounds(timeText, 0, timeText.length, timeBounds)

            val xDate = 20f
            val xTime = 20f
            val yDate = bitmap.height - 10f - dateBounds.height()
            val yTime = yDate - 30f - timeBounds.height()

            // Calculate background rectangles for date and time
            val dateBackgroundRect =
                RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect =
                RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color =
                backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
            canvas.drawRect(dateBackgroundRect, backgroundPaint)
            canvas.drawRect(timeBackgroundRect, backgroundPaint)

            // Draw the date and time text with stroke
            canvas.drawText(dateText, xDate, yDate, strokePaint)
            canvas.drawText(dateText, xDate, yDate, paint)
            canvas.drawText(timeText, xTime, yTime, strokePaint)
            canvas.drawText(timeText, xTime, yTime, paint)

            return bitmap
        }
    }

    fun makeMutableBitmap(immutableBitmap: Bitmap): Bitmap {
        return Bitmap.createBitmap(
            immutableBitmap.width,
            immutableBitmap.height,
            Bitmap.Config.ARGB_8888
        ).also { mutableBitmap ->
            val canvas = Canvas(mutableBitmap)
            canvas.drawBitmap(immutableBitmap, 0f, 0f, null)
        }
    }

    fun createTempFiles(context : Context, bitmap: Bitmap): File? {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "temp_image_${System.currentTimeMillis()}.jpeg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }



}