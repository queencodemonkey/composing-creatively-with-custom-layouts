package rt.compose.custom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import rt.compose.custom.modifier.polarOffset
import rt.compose.custom.ui.theme.BlueGray900
import rt.compose.custom.ui.theme.CustomLayoutTheme
import rt.compose.custom.ui.theme.GreenA200

/**
 * Launcher activity for Compose custom layout modifier example.
 */
class PolarCoordinatesActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      CustomLayoutTheme {
        Surface(
          color = BlueGray900,
          modifier = Modifier.fillMaxSize()
        ) {
          // Animation of small heart around large heart as a function of
          // θ from polar coordinates (r,θ).
          val transition = rememberInfiniteTransition(label = "Heart animation")
          val angle by transition.animateFloat(
            initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
              animation = tween(2000, easing = LinearEasing),
              repeatMode = RepeatMode.Restart
            ), label = "Rotation angle animation"
          )
          BoxWithConstraints(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
          ) {
            // Rotating heart: animated via [angle]
            Icon(
              modifier = Modifier
                .size(24.dp)
                .polarOffset(
                  radius = 100.dp,
                  angle = angle,
                  center = DpOffset(maxWidth / 2, maxHeight / 2)
                ),
              imageVector = Icons.Filled.Favorite,
              tint = GreenA200,
              contentDescription = "❤",
            )
            // Central heart
            Icon(
              modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
              imageVector = Icons.Filled.Favorite,
              tint = Color.Red,
              contentDescription = "❤",
            )
          }
        }
      }
    }
  }
}