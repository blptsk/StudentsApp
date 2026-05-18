package ru.tinkoff.favouritepersons.helpers

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * @author n.miroshkin
 */
object WireMockHelper {

    fun fileToString(
        path: String,
        context: Context = InstrumentationRegistry.getInstrumentation().context
    ): String {
        val inputStream = try {
            context.assets.open(path)
        } catch (e: Exception) {
            throw RuntimeException("❌ Asset file not found: $path", e)
        }

        return BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
            val content = reader.readText()
            if (content.isBlank()) {
                throw RuntimeException("❌ Asset file is empty or contains only whitespace: $path")
            }
            println("✅ Loaded JSON from $path, length: ${content.length}") // для отладки
            content
        }
    }
}
