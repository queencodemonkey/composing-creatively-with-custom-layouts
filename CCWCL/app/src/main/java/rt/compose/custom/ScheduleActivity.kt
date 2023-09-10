package rt.compose.custom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import rt.compose.schedule.ui.ScheduleRow
import rt.compose.custom.ui.theme.CustomLayoutTheme
import rt.compose.schedule.data.Session
import rt.compose.schedule.ui.ScheduleRowLayout
import java.time.LocalTime

class ScheduleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      CustomLayoutTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(vertical = 48.dp)
          ) {
            var dpsPerMinuteValue by remember { mutableFloatStateOf(3f) }
            // Implementation of a row of sessions in a schedule.
            // Swap Composable name to compare implementations
            //ScheduleRow(
            //ScheduleBoxRow(
            ScheduleRowLayout(
              modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(color = Color.White.copy(alpha = .5f)),
              sessions = SESSIONS,
              dpsPerMinute = dpsPerMinuteValue.dp,
            )
            Spacer(modifier = Modifier.height(48.dp))
            // DPs/minute slider to zoom in and out
            Slider(
              modifier = Modifier
                .padding(horizontal = 48.dp)
                .wrapContentHeight(),
              valueRange = 1f..5f,
              value = dpsPerMinuteValue,
              onValueChange = { dpsPerMinuteValue = it },
            )
          }
        }
      }
    }
  }

  companion object {
    private val SESSIONS = persistentListOf(
      Session(
        name = "Daily Dynamic Ladder Flow",
        instructor = "Briohny Smyth",
        type = Session.Type.Yoga,
        start = LocalTime.parse("07:00:00"),
        end = LocalTime.parse("08:00:00"),
      ),
      Session(
        name = "Shreds - Upper Body",
        instructor = "Sam Miller",
        type = Session.Type.Metcon,
        start = LocalTime.parse("08:30:00"),
        end = LocalTime.parse("10:00:00"),
      ),
      Session(
        name = "Doc's Fitness",
        instructor = "AJ Holland",
        type = Session.Type.Kettlebells,
        start = LocalTime.parse("11:00:00"),
        end = LocalTime.parse("12:00:00"),
      ),
      Session(
        name = "Lower Body Barre",
        instructor = "Corina Lindley",
        type = Session.Type.Barre,
        start = LocalTime.parse("12:30:00"),
        end = LocalTime.parse("13:15:00"),
      ),
      Session(
        name = "Beginning Hip Hop",
        instructor = "Marguerite Endsley",
        type = Session.Type.Dance,
        start = LocalTime.parse("14:00:00"),
        end = LocalTime.parse("15:15:00"),
      )
    )
  }
}