package rt.compose.schedule.data

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import rt.compose.schedule.ui.BlueA200
import rt.compose.schedule.ui.CyanA200
import rt.compose.schedule.ui.DeepOrangeA400
import rt.compose.schedule.ui.DeepPurpleA400
import rt.compose.schedule.ui.OrangeA400
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

data class Session(
    val name: String,
    val instructor: String,
    val type: Type,
    val start: LocalTime,
    val end: LocalTime
) {
    val totalMinutes: Int
        get() = ChronoUnit.MINUTES.between(start, end).toInt()

    enum class Type(
        val backgroundColor: Color,
        val useLightText: Boolean = false,
    ) {
        Yoga(CyanA200),
        Barre(BlueA200),
        Dance(DeepOrangeA400, true),
        Metcon(OrangeA400),
        Kettlebells(DeepPurpleA400, true)
    }
}

fun Session.minutesBetween(time: LocalTime): Int = when {
    time < start -> abs(ChronoUnit.MINUTES.between(time, start)).toInt()
    time > end -> abs(ChronoUnit.MINUTES.between(end, time)).toInt()
    else -> 0
}

val ImmutableList<Session>.totalMinutes: Long
    get() {
        if (isEmpty()) return 0L
        val minTime = minBy(Session::start).start
        val maxTime = maxBy(Session::end).end
        return ChronoUnit.MINUTES.between(minTime, maxTime)
    }