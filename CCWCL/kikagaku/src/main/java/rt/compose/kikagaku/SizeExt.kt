package rt.compose.kikagaku

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

//
// This file contains extension methods for working with
// XSize classes.
//

/**
 * Round this [Size] to the nearest [IntSize].
 */
@Stable
fun Size.round() = IntSize(width.roundToInt(), height.roundToInt())