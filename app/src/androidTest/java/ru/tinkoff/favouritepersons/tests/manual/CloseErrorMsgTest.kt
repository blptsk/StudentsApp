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
class CloseErrorMsgTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun closeErrorMsgTest() = run {

        val firstName = "Иван"
        val lastName = "Иванов"
        val birthday = "1953-12-13"
        val emale = "ivan@ivan.ru"
        val phone = "+79456781234"
        val address = "Улица пушкина док колотушкина"
        val photoLink = "https://onliner/api/portraits/med/women/1.jpeg"
        val score = "13"

        with(MainScreen(this)) {
            step("Переход на экран редактирования студента") {
                verifyEmptyScreen()
                addPersonByManually()
            }
        }

        with(DetailsScreen(this)) {
            step("Ввод данных и клик по кнопке: Сохранить") {
                verifyScreenOpened()
                inputFirstName(firstName)
                inputLastName(lastName)
                inputGender("3")
                inputBirthday(birthday)
                inputMale(emale)
                inputPhone(phone)
                inputAddress(address)
                inputPhotoLink(photoLink)
                inputScore(score)
                clickSubmitBtn()
            }
            step("Проверка ошибки в поле: Пол") {
                checkError()
            }
            step("Проверка ошибки в поле: Пол") {
                checkError()
            }
            step("Проверка скрытия ошибки") {
                clearFieldGender()
                errorNotDisplayed()
            }
        }
    }
}
