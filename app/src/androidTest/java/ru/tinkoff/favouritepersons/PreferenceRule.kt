package ru.tinkoff.favouritepersons

import android.app.ActivityManager
import android.content.Context
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tomakehurst.wiremock.client.WireMock

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.tinkoff.favouritepersons.helpers.ClearTestData.clearDB
import ru.tinkoff.favouritepersons.helpers.ClearTestData.createDb

/**
 * @author n.miroshkin
 */
class PreferenceRule : TestRule {

    override fun apply(base: Statement, p1: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                println("TestRule Before Test")
                createDb()
                putUrl()
                base.evaluate() // our test works here
                println("TestRule After Test")
                clearDB()
            }
        }
    }

    private fun putUrl() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("demo_url", Context.MODE_PRIVATE)
            .edit()
            .putString("url", "http://localhost:5000")
            .commit()
    }

    private fun setUp() {
        WireMock.reset() // Сбрасывает все маппинги
        WireMock.resetAllScenarios()
    }
}


object Device {
    val testContext = InstrumentationRegistry.getInstrumentation().context
    fun finishAndRemoveTasks() {
        runOnUiThread {
            (testContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).run {
                appTasks.forEach(ActivityManager.AppTask::finishAndRemoveTask)
            }
        }
    }
}
