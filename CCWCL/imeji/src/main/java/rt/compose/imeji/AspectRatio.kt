package rt.compose.imeji

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs

/**
 * Representation of image aspect ratios used for
 * moving between preset/standard aspect rations
 * as well as the original aspect ratio when cropping
 * and image.
 */
sealed class AspectRatio(val value: Float) {

    // region // Standard Aspect Ratios
    /** 16:9 */
    data object Landscape16by9 : AspectRatio(16f / 9f)

    /** 4:3 */
    data object Landscape4by3 : AspectRatio(4f / 3f)

    /** 3:2 */
    data object Landscape3by2 : AspectRatio(3f / 2f)

    /** 9:16 */
    data object Portrait9by16 : AspectRatio(9f / 16f)

    /**  3:4 */
    data object Portrait3by4 : AspectRatio(3f / 4f)

    /** 2:3 */
    data object Portrait2by3 : AspectRatio(2f / 3f)

    /** 1:1 */
    data object Square : AspectRatio(1f)

    // endregion

    /**
     * Image's original aspect ratio
     */
    data class Original(val width: Int, val height: Int) :
        AspectRatio(width.toFloat() / height.toFloat())

    companion object {
        /**
         * Recognized preset aspect ratios
         */
        internal val PRESETS =
            listOf(Landscape16by9, Landscape4by3, Landscape3by2, Portrait9by16, Portrait3by4, Portrait2by3, Square)
    }
}

// region // === Extensions/Utilities ===

/**
 * Returns the aspect ratio of this bitmap as an [AspectRatio].
 */
private val ImageBitmap.aspectRatio: AspectRatio
    get() = matchAspectRatio(width, height)

/**
 * Converts this [IntSize] to an [AspectRatio].
 */
private fun IntSize.toAspectRatio() = matchAspectRatio(width, height)

/**
 * Tries to match a given [width]/[height] to one of the aspect
 * ratio presets.
 */
private fun matchAspectRatio(width: Int, height: Int, delta: Float = 0.01f): AspectRatio {
    val ratio = width.toFloat() / height.toFloat()
    return AspectRatio.PRESETS.firstOrNull { aspectRatio -> abs(aspectRatio.value - ratio) <= delta }
        ?: AspectRatio.Original(width, height)
}

// endregion