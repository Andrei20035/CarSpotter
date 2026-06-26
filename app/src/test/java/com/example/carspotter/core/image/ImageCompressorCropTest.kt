package com.example.carspotter.core.image

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

/**
 * Pure-JVM tests for [ImageCompressor.computeCropRect].
 *
 * The container aspect ratio used throughout matches CarParams (1080×1350 == 375/468).
 */
class ImageCompressorCropTest {

    // Container dimensions in pixels (arbitrary but with the correct 4:5 aspect ratio).
    private val cw = 1080f
    private val ch = 1350f

    // Target aspect ratio for assertions (same as CarParams).
    private val targetAr = cw / ch

    private fun crop(
        imgW: Int, imgH: Int,
        scale: Float = 1f,
        offsetX: Float = 0f,
        offsetY: Float = 0f,
    ) = ImageCompressor.computeCropRect(imgW, imgH, cw, ch, scale, offsetX, offsetY)

    // Helpers -----------------------------------------------------------

    private fun assertAspectRatioApprox(rect: CropRect, tolerance: Float = 0.02f) {
        val ar = rect.width.toFloat() / rect.height.toFloat()
        assertTrue(
            "Expected crop AR ≈ $targetAr but was $ar (rect=$rect)",
            abs(ar - targetAr) <= tolerance,
        )
    }

    // Tests -------------------------------------------------------------

    /**
     * Identity: no user interaction on a landscape image (wider than 4:5).
     * Expected: horizontal center-crop matching the container AR.
     */
    @Test
    fun `identity on landscape image matches center crop`() {
        val imgW = 4000; val imgH = 3000   // 4:3 landscape
        val rect = crop(imgW, imgH)

        // Center of crop must be close to image center horizontally.
        val cropCenterX = (rect.left + rect.right) / 2f
        assertTrue("Crop should be horizontally centered", abs(cropCenterX - imgW / 2f) < 2f)
        // Height should span the full image height (image is shorter proportionally).
        assertEquals(imgH, rect.height)
        // Width should match: imgH * targetAr.
        val expectedWidth = (imgH * targetAr).toInt()
        assertTrue("Width diff > 2px: expected ~$expectedWidth, got ${rect.width}", abs(rect.width - expectedWidth) <= 2)
        assertAspectRatioApprox(rect)
    }

    /**
     * Identity: no user interaction on a portrait image taller than 4:5.
     * Expected: vertical center-crop matching the container AR.
     */
    @Test
    fun `identity on tall portrait image crops height`() {
        val imgW = 3000; val imgH = 4500   // 2:3 portrait, taller than 4:5
        val rect = crop(imgW, imgH)

        // Center of crop must be close to image center vertically.
        val cropCenterY = (rect.top + rect.bottom) / 2f
        assertTrue("Crop should be vertically centered", abs(cropCenterY - imgH / 2f) < 2f)
        // Width should span the full image width.
        assertEquals(imgW, rect.width)
        // Height should match: imgW / targetAr.
        val expectedHeight = (imgW / targetAr).toInt()
        assertTrue("Height diff > 2px: expected ~$expectedHeight, got ${rect.height}", abs(rect.height - expectedHeight) <= 2)
        assertAspectRatioApprox(rect)
    }

    /**
     * Identity on an image that already has the exact container AR — crop == entire image.
     */
    @Test
    fun `identity on perfectly matching aspect ratio returns full image`() {
        val imgW = 1080; val imgH = 1350
        val rect = crop(imgW, imgH)

        assertEquals(0, rect.left)
        assertEquals(0, rect.top)
        assertEquals(imgW, rect.right)
        assertEquals(imgH, rect.bottom)
    }

    /**
     * Scale = 2, no offset: user zoomed in 2×. The visible region in image pixels should be
     * exactly half the size of the scale=1 crop (centered).
     */
    @Test
    fun `scale 2 no offset gives half-size centered crop`() {
        val imgW = 4000; val imgH = 3000
        val base = crop(imgW, imgH, scale = 1f)
        val zoomed = crop(imgW, imgH, scale = 2f)

        // The zoomed crop should be half the size.
        assertTrue("Width should halve at 2×", abs(zoomed.width  - base.width  / 2) <= 2)
        assertTrue("Height should halve at 2×", abs(zoomed.height - base.height / 2) <= 2)
        // And remain centered.
        val centerX = (zoomed.left + zoomed.right) / 2f
        val centerY = (zoomed.top + zoomed.bottom) / 2f
        assertTrue("Zoomed crop should be centered X", abs(centerX - imgW / 2f) < 2f)
        assertTrue("Zoomed crop should be centered Y", abs(centerY - imgH / 2f) < 2f)
        assertAspectRatioApprox(zoomed)
    }

    /**
     * Positive offsetX (pan right): the visible window in image space should shift left
     * (smaller left boundary values), i.e. we see more of the left side of the image.
     */
    @Test
    fun `positive offsetX shifts crop window to image left`() {
        val imgW = 4000; val imgH = 3000
        val base   = crop(imgW, imgH, scale = 2f, offsetX = 0f)
        val panned = crop(imgW, imgH, scale = 2f, offsetX = 200f)

        assertTrue("Panning right should move crop window left in image space", panned.left < base.left)
        assertAspectRatioApprox(panned)
    }

    /**
     * Positive offsetY (pan down): the visible window shifts up in image space.
     */
    @Test
    fun `positive offsetY shifts crop window upward in image space`() {
        val imgW = 3000; val imgH = 4000
        val base   = crop(imgW, imgH, scale = 2f, offsetY = 0f)
        val panned = crop(imgW, imgH, scale = 2f, offsetY = 200f)

        assertTrue("Panning down should move crop window up in image space", panned.top < base.top)
        assertAspectRatioApprox(panned)
    }

    /**
     * Result must always be within image bounds regardless of extreme input.
     */
    @Test
    fun `result is always clamped to image bounds`() {
        val imgW = 2000; val imgH = 2500
        // Extreme offset that would push the window outside the image.
        val rect = crop(imgW, imgH, scale = 1f, offsetX = 100_000f, offsetY = 100_000f)

        assertTrue(rect.left >= 0)
        assertTrue(rect.top >= 0)
        assertTrue(rect.right <= imgW)
        assertTrue(rect.bottom <= imgH)
        assertTrue(rect.width >= 1)
        assertTrue(rect.height >= 1)
    }

    /**
     * The crop aspect ratio stays close to the container AR across a variety of images
     * and transform states.
     */
    @Test
    fun `aspect ratio is preserved across different images and transforms`() {
        // offsets must be within the clamp range that EditableImageContainer enforces,
        // otherwise the top/left boundary hits 0 and the AR check is meaningless.
        val cases = listOf(
            Triple(5000, 3000, Triple(1f,   0f,   0f)),
            Triple(3000, 5000, Triple(1.5f, 50f, -30f)),
            Triple(1080, 1350, Triple(2f,   0f,   0f)),
            // For 4032×3024 at scale=1.2: drawnH≈1350, maxOffsetY=(1350*1.2-1350)/2=135 → use 100
            Triple(4032, 3024, Triple(1.2f, 100f, 100f)),
        )
        for ((imgW, imgH, t) in cases) {
            val (scale, ox, oy) = t
            val rect = crop(imgW, imgH, scale, ox, oy)
            assertAspectRatioApprox(rect)
        }
    }
}
