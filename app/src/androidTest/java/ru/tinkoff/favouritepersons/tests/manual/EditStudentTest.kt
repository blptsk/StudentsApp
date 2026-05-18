package ru.tinkoff.favouritepersons.tests.manual

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.PreferenceRule
import ru.tinkoff.favouritepersons.helpers.ImgHelper
import ru.tinkoff.favouritepersons.helpers.StudentData
import ru.tinkoff.favouritepersons.helpers.WireMockHelper
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.screens.DetailsScreen
import ru.tinkoff.favouritepersons.screens.MainScreen

/**
 * @author n.miroshkin
 */
class EditStudentTest : TestCase()  {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun editStudentTest() = run {
        val editStudent = "editStudent"
        val newName = "Иосиф"
        val fullName  = "${newName} " +
                "${StudentData.getStudentData("responses/student_three.json").lastName}"

        step("Загрузка инфо первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(editStudent)
                    .whenScenarioStateIs(Scenario.STARTED)
                    .willSetStateTo("GetStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_three.json"))
                    )
            )
        }

        step("Получение фото первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/1.jpg")
                    .inScenario(editStudent)
                    .whenScenarioStateIs("GetStudent")
                    .willSetStateTo("GetPhoto")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/1.jpg"))
                    )
            )
        }

        with(MainScreen(this)) {
            step("Открытие экрана с данными студента") {
                clickPersonBtn()
                clickAddPersonByNetworkBtn()
                clickStudentByIndex(0)
            }
        }

        with(DetailsScreen(this)) {
            step("Открытие экрана с данными студента с изменением полей") {
                verifyScreenOpened()
                inputFirstName(newName)
                clickSubmitBtn()
            }
        }

        with(MainScreen(this)) {
            step("Проверка изменения имени") {
                verifyScreenIsNotEmpty()
                verifyStudentNameByIndex(0, fullName)
            }
        }
    }
}
