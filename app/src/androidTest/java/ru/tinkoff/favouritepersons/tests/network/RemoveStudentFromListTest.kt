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

/**
 * @author n.miroshkin
 */
class RemoveStudentFromListTest : TestCase() {

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun removeStudentFromListTest() = run {
        val scenarioV2 = "AddStudentsAndRemoveOne"

        step("Загрузка инфо первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(scenarioV2)
                    .whenScenarioStateIs(Scenario.STARTED)
                    .willSetStateTo("GetStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_one.json"))
                    )
            )
        }

        step("Получение фото первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/80.jpg")
                    .inScenario(scenarioV2)
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

        step("Загрузка инфо второго студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(scenarioV2)
                    .whenScenarioStateIs("GetPhoto")
                    .willSetStateTo("GetInfoSecondStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_two.json"))
                    )
            )
        }

        step("Получение фото второго студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/82.jpg")
                    .inScenario(scenarioV2)
                    .whenScenarioStateIs("GetInfoSecondStudent")
                    .willSetStateTo("GetPhotoSecondStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/82.jpg"))
                    )
            )
        }

        step("Загрузка инфо третьего студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(scenarioV2)
                    .whenScenarioStateIs("GetPhotoSecondStudent")
                    .willSetStateTo("GetInfoThirdStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_three.json"))
                    )
            )
        }

        step("Получение фото третьего студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/1.jpg")
                    .inScenario(scenarioV2)
                    .whenScenarioStateIs("GetInfoThirdStudent")
                    .willSetStateTo("GetPhotoThirdStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/1.jpg"))
                    )
            )
        }

        with(MainScreen(this)) {
            step("Добавление студентов") {
                verifyEmptyScreen()
                clickPersonBtn()
                clickAddPersonByNetworkBtn()
                clickAddPersonByNetworkBtn()
                clickAddPersonByNetworkBtn()
                clickPersonBtn()
            }
            step("Проверка изменения кол-ва студентов посде добавления") {
                checkStudentAmount(3)
            }

            step("Удаление второго студента") {
                removeStudentByIndex(1)
            }

            step("Проверка изменения кол-ва студентов посде удаления") {
                checkStudentAmount(2)
            }

        }

    }
}
