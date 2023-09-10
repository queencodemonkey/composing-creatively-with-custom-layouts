@file:Suppress("unused")

package rt.compose.schedule.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import rt.compose.schedule.data.Session
import rt.compose.schedule.data.Session.Type
import rt.compose.schedule.data.minutesBetween
import rt.compose.schedule.data.totalMinutes
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt


//
// This file contains different methods for implementing a timetable row
// to show the relative pros/cons but with an obvious preference for the
// custom layout version.
//

/**
 * Single schedule track. Implemented via [Row].
 *
 * [Spacer] instances are used for gaps between sessions.
 * Note: this is an example for presentation purpose and not a rigorous implementation
 * as it does not account for cases such as session overlap etc.
 *
 * @param sessions List of sessions; the data
 * @param modifier The modifier to be applied to the layout
 * @param dpsPerMinute Essentially the zoom/scale at which we are displaying time on-screen
 */
@Composable
fun ScheduleRow(
  sessions: ImmutableList<Session>,
  modifier: Modifier = Modifier,
  dpsPerMinute: Dp,
) {
  Row(
    modifier = modifier
      .fillMaxSize()
      .horizontalScroll(rememberScrollState()),
  ) {
    var lastTime: LocalTime? = null
    for (session in sessions) {
      lastTime?.let {
        // Calculate spacer width for free time between sessions.
        val spacerMinutes: Int = session.minutesBetween(it)
        val spacerWidth: Dp = dpsPerMinute * spacerMinutes
        Spacer(modifier = Modifier.width(spacerWidth))
      }
      val width = dpsPerMinute * session.totalMinutes
      key(session.hashCode()) {
        Session(
          modifier = Modifier
            .width(width)
            .fillMaxHeight(),
          session = session
        )
      }
      lastTime = session.end
    }
  }
}

/**
 * Single schedule track. Implemented via [Box].
 *
 * Position is specified via calculating an absolute offset.
 *
 * @param sessions List of sessions; the data
 * @param modifier The modifier to be applied to the layout
 * @param dpsPerMinute Essentially the zoom/scale at which we are displaying time on-screen
 */
@Composable
fun ScheduleBoxRow(
  sessions: ImmutableList<Session>,
  modifier: Modifier = Modifier,
  dpsPerMinute: Dp,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .horizontalScroll(rememberScrollState()),
  ) {
    if (sessions.isNotEmpty()) {
      // Convert session start times to X-values and use those
      // as absoluteOffset values.
      val startTime = sessions.first().start
      for (session in sessions) {
        val x: Dp = session.minutesBetween(startTime) * dpsPerMinute
        val width: Dp = dpsPerMinute * session.totalMinutes
        key(session.hashCode()) {
          Session(
            modifier = Modifier
              .width(width)
              .absoluteOffset(x)
              .fillMaxHeight(),
            session = session
          )
        }
      }
    }
  }
}

/**
 * Single schedule track. Implemented via a custom [Layout].
 *
 * Note: here we can take advantage of phased state reads by
 * separating the state calculation for measure/placement out of
 * composition and into the layout phase thus allowing for
 * some optimization by Compose.
 *
 * @param sessions List of sessions; the data
 * @param modifier The modifier to be applied to the layout
 * @param dpsPerMinute Essentially the zoom/scale at which we are displaying time on-screen
 */
@Composable
fun ScheduleRowLayout(
  sessions: ImmutableList<Session>,
  modifier: Modifier = Modifier,
  dpsPerMinute: Dp,
) {
  Layout(
    modifier = modifier.horizontalScroll(rememberScrollState()),
    content = {
      for (session in sessions) {
        key(session.hashCode()) {
          Session(
            modifier = Modifier
              .fillMaxHeight()
              .layoutId(session),
            session = session
          )
        }
      }
    },
    measurePolicy = { measurables, constraints ->

      // region // === Measurement Phase ===

      val (placeables, sessionStartTimes) = measurables.map { measurable ->
        val session = measurable.layoutId as Session
        val minutes = session.totalMinutes
        val widthPx = (minutes * dpsPerMinute).roundToPx()
        val sessionConstraints = constraints.copy(minWidth = widthPx, maxWidth = widthPx)
        measurable.measure(sessionConstraints) to session.start
      }.unzip()

      // endregion


      // region // === Placement Phase ===
      val pxPerMinute = dpsPerMinute.toPx()
      val maxWidth = (sessions.totalMinutes * pxPerMinute).roundToInt()

      layout(maxWidth, constraints.maxHeight) {
        val xCoordinates: List<Int> =
          if (sessionStartTimes.isEmpty()) {
            emptyList()
          } else {
            val initialTime = sessionStartTimes.first()
            sessionStartTimes.map { sessionStartTime ->
              (ChronoUnit.MINUTES.between(initialTime, sessionStartTime) * pxPerMinute).roundToInt()
            }
          }
        placeables.forEachIndexed { index, placeable ->
          placeable.placeRelative(x = xCoordinates[index], y = 0)
        }
      }
      // endregion
    }
  )
}


//region // ==== Test Session Data ====

private val SESSIONS_TEST_LIST = persistentListOf(
  Session(
    name = "Daily Dynamic Ladder Flow",
    instructor = "Briohny Smyth",
    type = Type.Yoga,
    start = LocalTime.parse("07:00:00"),
    end = LocalTime.parse("08:00:00"),
  ),
  Session(
    name = "Shreds - Upper Body",
    instructor = "Sam Miller",
    type = Type.Metcon,
    start = LocalTime.parse("08:30:00"),
    end = LocalTime.parse("10:00:00"),
  ),
  Session(
    name = "Doc's Fitness",
    instructor = "AJ Holland",
    type = Type.Kettlebells,
    start = LocalTime.parse("11:00:00"),
    end = LocalTime.parse("12:00:00"),
  ),
  Session(
    name = "Beginner Barre",
    instructor = "Corina Lindley",
    type = Type.Barre,
    start = LocalTime.parse("12:30:00"),
    end = LocalTime.parse("13:15:00"),
  )
)

//endregion