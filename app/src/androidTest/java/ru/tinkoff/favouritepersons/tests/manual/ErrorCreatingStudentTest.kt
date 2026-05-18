package ru.tinkoff.favouritepersons.tests.manual

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.PreferenceRule
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.screens.DetailsScreen
import ru.tinkoff.favouritepersons.screens.MainScreen

/**
 * @author n.miroshkin
 */
class ErrorCreatingStudentTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun errorCreatingStudentTest() = run {
        with(MainScreen(this)) {
            step("Переход на экран редактирования студента") {
                verifyEmptyScreen()
                addPersonByManually()
            }
        }

        with(DetailsScreen(this)) {
            step("Тап на кнопку: Сохранить") {
                verifyScreenOpened()
                clickSubmitBtn()
            }
            step("Проверка ошибки в поле: Пол") {
                checkError()
            }
        }
    }
}
