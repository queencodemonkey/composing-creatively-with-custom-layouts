package rt.compose.custom

import android.content.res.Configuration
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import rt.compose.custom.ui.theme.CustomLayoutTheme
import rt.compose.imeji.ImejiView
import java.io.IOException

/**
 * Launcher activity for Compose custom canvas drawing example.
 */
class ImejiActivity : ComponentActivity() {

  // region // === Activity overrides ===
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      CustomLayoutTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          var imageUri by rememberSaveable {
            mutableStateOf<Uri?>(null)
          }
          val imageBitmap = remember(imageUri) {
            imageUri?.let { uri ->
              try {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                  .asImageBitmap()
              } catch (io: IOException) {
                null
              }
            }
          }

          val photoPickerContract = ActivityResultContracts.PickVisualMedia()
          val launcher = rememberLauncherForActivityResult(photoPickerContract) { uri ->
            imageUri = uri
          }

          Scaffold(floatingActionButton = {
            PickPhotoFab(onClick = { launcher.pickPhoto() })
          }) { paddingValues ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
              AnimatedVisibility(visible = imageBitmap != null) {
                imageBitmap?.let {
                  ImejiContent(
                    modifier = Modifier.padding(24.dp),
                    image = imageBitmap
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // endregion

  // region === Photo Picker Composables + Helpers ===

  /**
   * FAB to initiate Photo Picker
   */
  @Composable
  private fun PickPhotoFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
  ) {
    FloatingActionButton(
      modifier = modifier,
      onClick = onClick
    ) {
      Icon(Icons.Filled.Add, contentDescription = getString(R.string.cd_button_photo_select))
    }
  }


  /**
   * Launches the Photo Picker with the appropriate contract.
   */
  private fun ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>.pickPhoto() {
    launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
  }

  //endregion
}

// region // === Content Composables ===

/**
 * Container for canvas-drawing example of
 * a Google-Photos-like photo cropping experience.
 *
 * @param image Previously loaded bitmap which will be cropped
 * @param modifier The modifier to be applied to the layout
 */
@Composable
fun ImejiContent(
  image: ImageBitmap,
  modifier: Modifier = Modifier
) {
  val configuration = LocalConfiguration.current
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    // Fit crop area differently in portrait/landscape for usability.
    val imageModifier = Modifier
      .align(Alignment.Center)
      .then(
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          Modifier
            .fillMaxHeight()
            .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true)
        } else {
          Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1f)

        }
      )
    // Custom composable that provides cropping experience
    ImejiView(
      image = image,
      modifier = imageModifier
        .border(width = 1.dp, color = Color.White)
    )
  }
}

// engregion