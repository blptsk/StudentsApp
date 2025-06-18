package tests

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.Test
import screens.MainScreen

/**
 * @author a.m.sidenov
 */
class PersonAddByNetworkTests : BaseTest() {

    @Test
    fun noPersonsMessageNotDisplayedTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/first_person_from_network_response.json"))
                )
        )

        with(MainScreen(this)) {
            addPersonByNetwork()
            checkNoPersonsMessageNotDisplayed()
        }
    }

    @Test
    fun deletePersonTest() = run {

        val scenario = "deletePerson"
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("secondPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/first_person_from_network_response.json"))
                )
        )
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs("secondPerson")
                .willSetStateTo("thirdPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/second_person_from_network_response.json"))
                )
        )
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs("thirdPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/third_person_from_network_response.json"))
                )
        )

        with(MainScreen(this)) {
            addPersonByNetworkManyTimes(3)
            deletePersonOnPosition(0)
            checkPersonListSize(2)
            checkPersonNameNotDisplayed("Dobrolyub Stupka", 0)
        }
    }

    @Test
    fun defaultSortTest() = run {
        with(MainScreen(this)) {
            clickSortButton()
            checkDefaultSortIsSelected()
        }
    }

    @Test
    fun sortByAgeTest() = run {
        val scenario = "deletePerson"
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("secondPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/first_person_from_network_response.json"))
                )
        )
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs("secondPerson")
                .willSetStateTo("thirdPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/second_person_from_network_response.json"))
                )
        )
        stubFor(
            get("/api/")
                .inScenario(scenario)
                .whenScenarioStateIs("thirdPerson")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(fileToString("responses/third_person_from_network_response.json"))
                )
        )

        with(MainScreen(this)) {
            addPersonByNetworkManyTimes(3)
            clickSortButton()
            clickSortByAge()
            checkAgeOfPeopleInList(arrayOf("122","71", "52"))
        }
    }

    @Test
    fun noInternetConnectionTest() = run {
        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                )
        )

        with(MainScreen(this)) {
            addPersonByNetwork()
            checkNoInternetToastDisplayed()
        }
    }
}
