package rt.compose.kikagaku

import androidx.compose.ui.geometry.Size
import org.junit.Test

import org.junit.Assert.*

/**
 * Tests for XSize extensions
 */
class SizeExtTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun size_toIntSize_rounds_down() {
    val size = Size(0.2f, 0.4f)
    val intSize = size.round()
    assertEquals(intSize.width, 0)
    assertEquals(intSize.height, 0)
  }
  @Test
  fun size_toIntSize_rounds_up() {
    val size = Size(0.5f, 0.9f)
    val intSize = size.round()
    assertEquals(intSize.width, 1)
    assertEquals(intSize.height, 1)
  }

  @Test
  fun size_toIntSize_integral_value() {
    val size = Size(2f, 4f)
    val intSize = size.round()
    assertEquals(intSize.width, 2)
    assertEquals(intSize.height, 4)
  }
}