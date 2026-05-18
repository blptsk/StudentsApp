package ru.tinkoff.favouritepersons.helpers

import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONObject

/**
 * @author n.miroshkin
 */
object StudentData {

    fun getStudentData(fileName: String): StudentMockData {
        val context = InstrumentationRegistry.getInstrumentation().context
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }

        val root = JSONObject(jsonString)
        val firstResult = root.getJSONArray("results").getJSONObject(0)
        val nameObj = firstResult.getJSONObject("name")
        val dobObj = firstResult.getJSONObject("dob")
        val gender = firstResult.getString("gender")

        return StudentMockData(
            firstName = nameObj.getString("first"),
            lastName = nameObj.getString("last"),
            dobDate = dobObj.getString("date").substringBefore("T"),
            gender = gender.take(1)
        )
    }

}

data class StudentMockData(
    val firstName: String,
    val lastName: String,
    val dobDate: String,
    val gender: String
)
