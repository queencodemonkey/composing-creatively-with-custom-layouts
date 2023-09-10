package rt.compose.imeji

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.width
import rt.compose.imeji.gestures.detectTransformGestures
import rt.compose.kikagaku.round
import kotlin.math.max
import kotlin.math.min

/**
 * View that simulates a crop interface that allows a user to resize and change
 * the crop of an image.
 *
 * Note: not actually functional but an example of an advanced usage of
 * custom canvas drawing with Compose.
 */
@Composable
fun ImejiView(
  image: ImageBitmap,
  modifier: Modifier = Modifier,
  colors: ImejiColors = ImejiDefaults.colors(),
  dimens: ImejiDimens = ImejiDefaults.dimens()
) {
  BoxWithConstraints(modifier = modifier) {

    // region // ==== Image/crop bounds calculations ====
    val imageSizeDp = with(LocalDensity.current) {
      DpSize(width = image.width.toDp(), height = image.height.toDp())
    }
    val inset = dimens.cropAreaInset
    val availableBoundsDp: DpRect = remember(maxWidth, maxHeight) {
      DpRect(
        origin = DpOffset(inset, inset),
        size = DpSize(maxWidth - (inset * 2f), minWidth - (inset * 2f))
      )
    }
    // Calculate the minimum scaling permitted when pinching to zoom in/out.
    val minScale = remember(image, availableBoundsDp) {
      val actualMinScale =
        min(
          availableBoundsDp.width / imageSizeDp.width,
          availableBoundsDp.height / imageSizeDp.height
        )
      min(actualMinScale, 1f)
    }

    val density = LocalDensity.current.density
    var scale by remember(minScale) { mutableFloatStateOf(minScale) }
    // Calculate bounds of image as actually drawn on screen/canvas
    var drawBoundsPx by remember(image, scale) {
      val scaledImageSize = imageSizeDp * scale
      fun initialPositionFromSize(maxSize: Dp, size: Dp) =
        max((maxSize.value * density - size.value * density) * 0.5f, 0f)
      mutableStateOf(
        Rect(
          offset = Offset(
            x = initialPositionFromSize(availableBoundsDp.width, scaledImageSize.width),
            y = initialPositionFromSize(availableBoundsDp.height, scaledImageSize.height),
          ),
          size = Size(
            scaledImageSize.width.value * density,
            scaledImageSize.height.value * density
          )
        )
      )
    }

    //endregion

    var showGrid by remember { mutableStateOf(false) }
    var gestureToggle by remember { mutableStateOf(false) }
    val gridStrokeColor by animateColorAsState(
      label = "Grid Animation",
      targetValue = if (showGrid) colors.gridStroke else Color.Transparent,
    )
    val gridStrokeWidthPx = with(LocalDensity.current) { dimens.gridStrokeWidth.toPx() }

    Canvas(
      modifier = Modifier
        .padding(all = inset)
        .fillMaxSize()
        .graphicsLayer(clip = !showGrid)
        .border(color = colors.gridStroke, width = dimens.gridStrokeWidth)
        // Out-of-the-box gesture modifier
//                .transformable(rememberTransformableState { zoomChange, panChange, _ ->
//                    scale *= zoomChange
//                    drawBoundsPx = drawBoundsPx.translate(panChange)
//                }),
        // Low-level gesture detection
        .pointerInput(gestureToggle, image) {
          detectTransformGestures(
            onTransformStart = {
              showGrid = true
            },
            onTransformEnd = {
              showGrid = false
              gestureToggle = !gestureToggle
            }
          ) { _, panChange, zoomChange, _ ->
            // Validity check for scale
            val newScale = scale * zoomChange
            if (newScale >= minScale) {
              scale = newScale
            }
            // TODO: Validity check for bounds
            drawBoundsPx = drawBoundsPx.translate(panChange)
          }
        }
    ) {

      drawImage(
        image = image,
        dstOffset = drawBoundsPx.topLeft.round(),
        dstSize = drawBoundsPx.size.round(),
      )
      drawGrid(color = gridStrokeColor, strokeWidth = gridStrokeWidthPx)
    }
  }
}

// region // === Helper methods/extensions ===

fun DrawScope.drawGrid(color: Color, strokeWidth: Float = Stroke.HairlineWidth) {
  with(size) {
    /**
     * Draw grid line from [start] to [end].
     */
    fun drawGridLine(start: Offset, end: Offset) {
      drawLine(
        color = color,
        strokeWidth = strokeWidth,
        start = start,
        end = end
      )
    }

    // Draw vertical grid lines.
    val widthThird = width / 3f
    drawGridLine(start = Offset(widthThird, 0f), end = Offset(widthThird, height))
    val widthTwoThird = width * 2f / 3f
    drawGridLine(start = Offset(widthTwoThird, 0f), end = Offset(widthTwoThird, height))
    // Draw horizontal grid lines.
    val heightThird = height / 3f
    drawGridLine(start = Offset(0f, heightThird), end = Offset(width, heightThird))
    val heightTwoThird = height * 2f / 3f
    drawGridLine(start = Offset(0f, heightTwoThird), end = Offset(width, heightTwoThird))
  }
}

// endregion


// region // ==== ImejiView Theming Resources ====

// region // === Theme components ===

/**
 * Definition of color slots for [ImejiView] styling
 */
@Immutable
data class ImejiColors internal constructor(
  /**
   * Crop grid stroke color
   */
  val gridStroke: Color,
  /**
   * Color of scrim surrounding crop area
   */
  val scrim: Color,
)

/**
 * Definition of dimension slots for [ImejiView] styling
 */
data class ImejiDimens internal constructor(
  /**
   * Crop grid stroke width
   */
  val gridStrokeWidth: Dp,
  /**
   * Padding between the edge of the [ImejiView] to the crop area.
   */
  val cropAreaInset: Dp
)

// endregion

/**
 * Definition of default values for [ImejiView] styling properties
 */
object ImejiDefaults {

  /**
   * Returns [ImejiColors] instance with defaults.
   */
  @Composable
  fun colors(
    gridStroke: Color = Color.White,
    scrim: Color = Color(0x80263238)
  ) = ImejiColors(gridStroke = gridStroke, scrim = scrim)


  /**
   * Returns [ImejiDimens] instance with defaults.
   */
  @Composable
  fun dimens(
    gridStrokeWidth: Dp = 1.dp,
    cropAreaInset: Dp = 24.dp,
  ) = ImejiDimens(gridStrokeWidth = gridStrokeWidth, cropAreaInset = cropAreaInset)
}

// endregion

