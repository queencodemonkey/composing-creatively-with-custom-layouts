package rt.compose.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import rt.compose.schedule.data.Session
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Preview of the Session Composable.
 */
@Preview(widthDp = 240, heightDp = 240)
@Composable
fun SessionPreview() {
  Box(
    modifier = Modifier
      .background(BlueGray900)
      .padding(24.dp)
  ) {
    val session = Session(
      name = "Daily Dynamic Ladder Flow",
      instructor = "Briohny Smyth",
      type = Session.Type.Yoga,
      start = LocalTime.parse("07:00:00"),
      end = LocalTime.parse("08:00:00"),
    )
    Session(
      session = session,
      modifier = Modifier.fillMaxSize()
    )
  }
}

/**
 * Representation of a Session event in a timetable.
 *
 * @param session Session data
 * @param modifier The modifier to be applied to the layout
 * @param colors Colors to apply to this Session Composable as defined by [SessionColors]
 * @param dimens Dimensions to apply to this Session Composable as defined by [SessionDimens]
 * @param backgroundShape Shape for drawing this Session Composables's background
 */
@Composable
fun Session(
  session: Session,
  modifier: Modifier = Modifier,
  colors: SessionColors = SessionDefaults.colors(),
  dimens: SessionDimens = SessionDefaults.dimens(),
  backgroundShape: Shape = SessionDefaults.backgroundShape(dimens.cornerSize)
) {
  Box(
    modifier = modifier
      .background(
        color = session.type.backgroundColor,
        shape = backgroundShape
      )
      .border(
        width = dimens.borderStrokeWidth,
        color = colors.border,
        shape = backgroundShape
      )
      .padding(dimens.padding)
  ) {
    // Session info text
    Text(text = buildAnnotatedString {
      withStyle(style = ParagraphStyle(lineHeight = dimens.lineHeight)) {
        withStyle(
          style = SpanStyle(
            fontSize = dimens.textSize,
            color = if (session.type.useLightText) colors.lightText else colors.darkText
          )
        ) {
          append("${session.toFormattedTimeString()}\n\n")
          withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
            append("${session.instructor}\n")
          }
          withStyle(style = SpanStyle(fontSize = dimens.titleTextSize, fontWeight = FontWeight.Medium)) {
            append("${session.name}\n")
          }
          withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
            append(session.type.name)
          }
        }
      }
    })
  }
}


// region // ==== Data display helper properties/extensions ====

private val SESSION_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

/**
 * Return session time formatted string.
 */
fun Session.toFormattedTimeString() =
  "${SESSION_TIME_FORMATTER.format(start)} - ${SESSION_TIME_FORMATTER.format(end)}"

// endregion


// region // ==== Session Theming Resources ====

// region // === Theme components ===

@Immutable
data class SessionColors internal constructor(
  val border: Color,
  val darkText: Color,
  val lightText: Color,
)

@Immutable
data class SessionDimens internal constructor(
  val borderStrokeWidth: Dp,
  val padding: PaddingValues,
  val cornerSize: Dp,
  val textSize: TextUnit,
  val titleTextSize: TextUnit,
  val lineHeight: TextUnit,
)


// endregion


/**
 * Definition of styling defaults for Session Composables.
 */
object SessionDefaults {

  private val CORNER_SIZE_DEFAULT = 8.dp

  /**
   * Returns [SessionColors] instance with defaults.
   */
  @Composable
  fun colors(
    border: Color = Color.White.copy(alpha = 0.5f),
    darkText: Color = Color.Black,
    lightText: Color = Color.White
  ) = SessionColors(
    border = border,
    darkText = darkText,
    lightText = lightText
  )

  /**
   * Returns [SessionDimens] instance with defaults.
   */
  @Composable
  fun dimens(
    borderStrokeWidth: Dp = 4.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    cornerSize: Dp = CORNER_SIZE_DEFAULT,
    textSize: TextUnit = 16.sp,
    titleTextSize: TextUnit = 18.sp,
    lineHeight: TextUnit = 1.5.em,
  ) = SessionDimens(
    borderStrokeWidth = borderStrokeWidth,
    padding = padding,
    cornerSize = cornerSize,
    textSize = textSize,
    titleTextSize = titleTextSize,
    lineHeight = lineHeight,
  )

  /**
   * Default background shape for Session Composables.
   */
  val backgroundShape: (Dp) -> Shape = fun(cornerSize: Dp) = RoundedCornerShape(cornerSize)
}

// endregion