package ru.tinkoff.favouritepersons.helpers

import androidx.test.platform.app.InstrumentationRegistry

/**
 * @author n.miroshkin
 */
object ImgHelper {

    fun getBytes(path : String) : ByteArray  {
        val testContext = InstrumentationRegistry.getInstrumentation().context
        val imageStudent = testContext.assets.open(path).readBytes()
        return imageStudent
    }


}
