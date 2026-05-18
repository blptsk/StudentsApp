package ru.tinkoff.favouritepersons.tests.network

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.PreferenceRule
import ru.tinkoff.favouritepersons.helpers.ImgHelper
import ru.tinkoff.favouritepersons.helpers.WireMockHelper
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.screens.MainScreen

class AddStudentTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addStudentTest() = run {
        val scenario = "AddStudent"

        step("Загрузка инфо студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(scenario)
                    .whenScenarioStateIs(Scenario.STARTED)
                    .willSetStateTo("GetStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_one.json"))
                    )
            )
        }

        step("Получение фото студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/80.jpg")
                    .inScenario(scenario)
                    .whenScenarioStateIs("GetStudent")
                    .willSetStateTo("GetPhoto")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/80.jpg"))
                    )
            )
        }

        with(MainScreen(this)) {
            step("Добавление студента") {
                verifyEmptyScreen()
                addPersonByNetwork()
            }
            step(
                "Проверяем что сообщение об отсутствии студентов" +
                        " в списке более не отображается"
            ) {
                verifyScreenIsNotEmpty()
            }
        }
    }
}
