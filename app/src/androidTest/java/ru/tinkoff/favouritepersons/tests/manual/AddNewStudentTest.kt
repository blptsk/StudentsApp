package ru.tinkoff.favouritepersons.tests.manual

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.PreferenceRule
import ru.tinkoff.favouritepersons.helpers.ImgHelper
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.screens.DetailsScreen
import ru.tinkoff.favouritepersons.screens.MainScreen

/**
 * @author n.miroshkin
 */
class AddNewStudentTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addNewStudentTest() = run {
        val firstName = "Иван"
        val lastName = "Иванов"
        val gender = "М"
        val birthday = "1953-12-13"
        val age = "72"
        val emale = "ivan@ivan.ru"
        val phone = "+79456781234"
        val address = "Улица пушкина док колотушкина"
        val photoLink = "https://onliner/api/portraits/med/women/1.jpeg"
        val score = "13"
        val fullName = "${firstName} ${lastName}"

        step("Получение фото первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/1.jpg")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/1.jpg"))
                    )
            )
        }

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
                inputGender(gender)
                inputBirthday(birthday)
                inputMale(emale)
                inputPhone(phone)
                inputAddress(address)
                inputPhotoLink(photoLink)
                inputScore(score)
                clickSubmitBtn()
            }
        }

        with(MainScreen(this)) {
            step("Проверка созданого студента на главном экране") {
                verifyScreenIsNotEmpty()
                verifyStudentNameByIndex(0, fullName)
                verifyGenderAndAgeByIndex(0, gender, age)
                verifyEmaleByIndex(0, emale)
                verifyPhoneByIndex(0, phone)
                verifyAddressByIndex(0, address)
                verifyScoreByIndex(0, score)
            }
        }
    }
}
