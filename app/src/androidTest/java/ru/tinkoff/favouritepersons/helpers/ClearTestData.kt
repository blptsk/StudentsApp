package ru.tinkoff.favouritepersons.helpers

import androidx.test.platform.app.InstrumentationRegistry
import ru.tinkoff.favouritepersons.room.PersonDataBase

/**
 * @author n.miroshkin
 */

object ClearTestData {
    private lateinit var db: PersonDataBase

    fun createDb() {
        db = PersonDataBase.getDBClient(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    fun clearDB() {
        db.personsDao().clearTable()
    }
}
