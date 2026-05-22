package com.example.carspotter.core.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.abs
import kotlin.math.roundToInt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressor @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    data class Params(
        val maxWidthPx: Int,
        val maxHeightPx: Int,
        val jpegQuality: Int,
    ) {
        val aspectRatio: Float
            get() = maxWidthPx.toFloat() / maxHeightPx.toFloat()

        init {
            require(maxWidthPx > 0) { "maxWidthPx must be greater than 0" }
            require(maxHeightPx > 0) { "maxHeightPx must be greater than 0" }
            require(jpegQuality in 0..100) { "jpegQuality must be between 0 and 100" }
        }
    }

    data class CompressedImage(
        val bytes: ByteArray,
        val mimeType: String = JPEG_MIME_TYPE,
    )

    /**
     * Decodes, fixes EXIF orientation, downsizes and JPEG-compresses the image at [uri].
     *
     * The work runs on [Dispatchers.IO]. Invalid or unreadable images throw
     * [ImageCompressionException] with a user-safe message instead of crashing with a low-level
     * decoder error.
     */
    suspend fun compress(uri: Uri, params: Params): CompressedImage = withContext(Dispatchers.IO) {
        var decoded: Bitmap? = null
        var oriented: Bitmap? = null
        var cropped: Bitmap? = null
        var resized: Bitmap? = null

        try {
            val bounds = decodeBounds(uri)
            val options = BitmapFactory.Options().apply {
                inSampleSize = calculateInSampleSize(
                    width = bounds.outWidth,
                    height = bounds.outHeight,
                    maxWidthPx = params.maxWidthPx,
                    maxHeightPx = params.maxHeightPx,
                )
            }

            decoded = decodeBitmap(uri, options)
            oriented = applyExifOrientation(decoded, readExifOrientation(uri))
            cropped = centerCropToAspectRatio(oriented, params.aspectRatio)
            resized = resizeIfNeeded(cropped, params.maxWidthPx, params.maxHeightPx)
            val bytes = compressToJpeg(resized, params.jpegQuality)

            CompressedImage(bytes = bytes)
        } catch (exception: ImageCompressionException) {
            throw exception
        } catch (exception: Exception) {
            throw ImageCompressionException("Failed to compress image", exception)
        } finally {
            recycleIfDifferent(decoded, oriented, cropped, resized)
        }
    }

    suspend fun compressProfileImage(uri: Uri): CompressedImage =
        compress(uri, ProfileParams)

    suspend fun compressCarImage(uri: Uri): CompressedImage =
        compress(uri, CarParams)

    private fun decodeBounds(uri: Uri): BitmapFactory.Options {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        openInputStream(uri).use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            throw ImageCompressionException("Selected file is not a valid image")
        }
        return options
    }

    private fun decodeBitmap(uri: Uri, options: BitmapFactory.Options): Bitmap {
        return openInputStream(uri).use { input ->
            BitmapFactory.decodeStream(input, null, options)
        } ?: throw ImageCompressionException("Unable to decode selected image")
    }

    private fun readExifOrientation(uri: Uri): Int {
        return openInputStream(uri).use { input ->
            ExifInterface(input).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )
        }
    }

    private fun applyExifOrientation(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.postRotate(90f)
                matrix.preScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.postRotate(270f)
                matrix.preScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun centerCropToAspectRatio(bitmap: Bitmap, targetAspectRatio: Float): Bitmap {
        val currentAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        if (abs(currentAspectRatio - targetAspectRatio) < ASPECT_RATIO_EPSILON) {
            return bitmap
        }

        return if (currentAspectRatio > targetAspectRatio) {
            val cropWidth = (bitmap.height * targetAspectRatio)
                .roundToInt()
                .coerceIn(1, bitmap.width)
            val cropX = (bitmap.width - cropWidth) / 2
            Bitmap.createBitmap(bitmap, cropX, 0, cropWidth, bitmap.height)
        } else {
            val cropHeight = (bitmap.width / targetAspectRatio)
                .roundToInt()
                .coerceIn(1, bitmap.height)
            val cropY = (bitmap.height - cropHeight) / 2
            Bitmap.createBitmap(bitmap, 0, cropY, bitmap.width, cropHeight)
        }
    }

    private fun resizeIfNeeded(bitmap: Bitmap, maxWidthPx: Int, maxHeightPx: Int): Bitmap {
        if (bitmap.width <= maxWidthPx && bitmap.height <= maxHeightPx) return bitmap

        val scale = minOf(
            maxWidthPx.toFloat() / bitmap.width.toFloat(),
            maxHeightPx.toFloat() / bitmap.height.toFloat(),
        )
        val targetWidth = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val targetHeight = (bitmap.height * scale).toInt().coerceAtLeast(1)

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    private fun compressToJpeg(bitmap: Bitmap, quality: Int): ByteArray {
        val output = ByteArrayOutputStream()
        val compressed = output.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
        }
        if (!compressed) {
            throw ImageCompressionException("Unable to compress selected image")
        }
        return output.toByteArray()
    }

    private fun openInputStream(uri: Uri) =
        context.contentResolver.openInputStream(uri)
            ?: throw ImageCompressionException("Unable to open selected image")

    private fun recycleIfDifferent(vararg bitmaps: Bitmap?) {
        bitmaps.filterNotNull().distinctBy { System.identityHashCode(it) }.forEach { bitmap ->
            if (!bitmap.isRecycled) bitmap.recycle()
        }
    }

    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        maxWidthPx: Int,
        maxHeightPx: Int,
    ): Int {
        var inSampleSize = 1
        val halfWidth = width / 2
        val halfHeight = height / 2

        while (
            halfWidth / inSampleSize >= maxWidthPx &&
            halfHeight / inSampleSize >= maxHeightPx
        ) {
            inSampleSize *= 2
        }

        return inSampleSize
    }

    companion object {
        const val JPEG_MIME_TYPE = "image/jpeg"
        private const val ASPECT_RATIO_EPSILON = 0.01f

        val ProfileParams = Params(
            maxWidthPx = 512,
            maxHeightPx = 512,
            jpegQuality = 80
        )

        val CarParams = Params(
            maxWidthPx = 1080,
            maxHeightPx = 1350,
            jpegQuality = 82
        )
    }
}

class ImageCompressionException(
    message: String,
    cause: Throwable? = null,
) : IOException(message, cause)
