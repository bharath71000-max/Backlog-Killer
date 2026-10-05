package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Backlog Killer", appName)
  }

  @Test
  fun `verify roi formula calculation`() {
    val weightage = 9
    val weakness = 9
    val hours = 2.0
    val expectedRoi = (weightage * weakness) / hours
    assertEquals(40.5, expectedRoi, 0.01)
  }
}
