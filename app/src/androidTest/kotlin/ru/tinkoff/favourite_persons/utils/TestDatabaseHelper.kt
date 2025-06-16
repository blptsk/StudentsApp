package ru.tinkoff.favourite_persons.utils

import androidx.test.platform.app.InstrumentationRegistry
import ru.tinkoff.favouritepersons.room.PersonDataBase

object TestDatabaseHelper {
    private lateinit var db: PersonDataBase

    fun setup() {
        db = PersonDataBase.getDBClient(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    fun clearDatabase() {
        db.personsDao().clearTable()
    }
} 