package rt.compose.custom.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


/**
 * Custom layout modifier that uses polar coordinates,
 * ([radius], [angle]) to place an element within its parent,
 * centered around [center].
 */
fun Modifier.polarOffset(
  radius: Dp,
  angle: Float,
  center: DpOffset = DpOffset.Zero
) = layout { measurable, constraints ->
  // Measure the element
  val placeable = measurable.measure(constraints)
  // Place the element
  layout(placeable.width, placeable.height) {
    // Calculate XY from polar coordinates.
    val radiusPx = radius.toPx()
    val radians = Math.toRadians(angle.toDouble())
    val x = center.x.roundToPx() + (radiusPx * cos(radians)).roundToInt() - placeable.width / 2
    val y = center.y.roundToPx() + (radiusPx * sin(radians)).roundToInt() - placeable.height / 2
    placeable.placeRelative(x, y)
  }
}

/**
 * Preview displaying several, distinct polar
 * coordinates around the same center
 */
@Preview(widthDp = 384, heightDp = 216)
@Composable
fun PolarOffsetPreview() {
  BoxWithConstraints(
    modifier = Modifier
      .background(Color(0xFF263238))
      .padding(24.dp)
  ) {
    // Green Heart: 45 degrees
    Icon(
      modifier = Modifier
        .size(24.dp)
        .polarOffset(
          radius = 100.dp,
          angle = 45f,
          center = DpOffset(maxWidth / 2, maxHeight / 2)
        ),
      imageVector = Icons.Filled.Favorite,
      tint = Color(0xFF69F0AE),
      contentDescription = "❤",
    )
    // Line Heart: 180 degrees
    Icon(
      modifier = Modifier
        .size(24.dp)
        .polarOffset(
          radius = 100.dp,
          angle = 180f,
          center = DpOffset(maxWidth / 2, maxHeight / 2)
        ),
      imageVector = Icons.Filled.Favorite,
      tint = Color(0xFFC6FF00),
      contentDescription = "❤",
    )
    // Red Heart
    Icon(
      modifier = Modifier
        .size(60.dp)
        .align(Alignment.Center),
      imageVector = Icons.Filled.Favorite,
      tint = Color.Red,
      contentDescription = "❤",
    )
  }
}