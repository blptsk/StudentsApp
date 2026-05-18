package ru.tinkoff.favouritepersons.tests.internet

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.PreferenceRule
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.screens.MainScreen

/**
 * @author n.miroshkin
 */
class NoInternetTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5005)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun noInternetTest() = run {

        with(MainScreen(this)) {
            step("Тап на кнопку добавления студента без интернета") {
                verifyEmptyScreen()
                addPersonByNetwork()
            }
            step("Проверка показа баннера об отсутствии инета") {
                checkSnackbar()
            }
        }
    }
}
