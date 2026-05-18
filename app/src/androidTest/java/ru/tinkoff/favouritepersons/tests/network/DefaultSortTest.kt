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
import ru.tinkoff.favouritepersons.screens.SortScreen

/**
 * @author n.miroshkin
 */
class DefaultSortTest : TestCase(){

    @get: Rule(order = 1)
    val preferenceRule = PreferenceRule()

    @get: Rule(order = 2)
    val mock = WireMockRule(5000)

    @get: Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun defaultSortTest() = run {
        with(MainScreen(this)) {
            step("Открытие мень сортировки") {
                verifyEmptyScreen()
                clickSortBnt()
            }
        }
        with(SortScreen(this)) {
            verifySortPanelOpened()
            verifyDefaultPontIsActive()
        }
    }

    @Test
    fun sortByAgeTest() = run {
        val sortedScenario = "sortedScenario"

        step("Загрузка инфо первого студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(sortedScenario)
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
                    .inScenario(sortedScenario)
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

        step("Загрузка инфо второго студента") {
            WireMock.stubFor(
                WireMock.get("/api/")
                    .inScenario(sortedScenario)
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
                    .inScenario(sortedScenario)
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
                    .inScenario(sortedScenario)
                    .whenScenarioStateIs("GetPhotoSecondStudent")
                    .willSetStateTo("GetInfoThirdStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody(WireMockHelper.fileToString("responses/student_one.json"))
                    )
            )
        }

        step("Получение фото третьего студента") {
            WireMock.stubFor(
                WireMock.get("/api/portraits/med/women/80.jpg")
                    .inScenario(sortedScenario)
                    .whenScenarioStateIs("GetInfoThirdStudent")
                    .willSetStateTo("GetPhotoThirdStudent")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "image/jpeg")
                            .withBody(ImgHelper.getBytes("responses/80.jpg"))
                    )
            )
        }
        with(MainScreen(this)) {
            step("Открытие мень сортировки") {
                verifyEmptyScreen()
                clickPersonBtn()
                clickAddPersonByNetworkBtn()
                clickAddPersonByNetworkBtn()
                clickAddPersonByNetworkBtn()
                clickPersonBtn()
                clickSortBnt()
            }
        }
        with(SortScreen(this)) {
            step("Выбрать сортировку по возрасту") {
                verifySortPanelOpened()
                clickAgePont()
            }
        }
        with(MainScreen(this)) {
            step("Проверка сортироваки студентов по возрасту") {
                verifySorterStudentByAge()
            }
        }

    }
}
