package ru.tinkoff.favourite_persons.utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.google.gson.Gson
import ru.tinkoff.favouritepersons.data.network.Info
import ru.tinkoff.favouritepersons.data.network.PersonDto
import ru.tinkoff.favouritepersons.data.network.Persons

object WireMockHelper {

    private const val API_PATH = "/api/"
    private const val SCENARIO_NAME = "PersonsScenario"
    private val gson = Gson()

    fun createServer(port: Int = 5000): WireMockServer {
        return WireMockServer(WireMockConfiguration.options().port(port)).apply {
            start()
            WireMock.configureFor("localhost", port)
        }
    }

    fun stopServer(server: WireMockServer) {
        server.stop()
    }

    fun resetStubs(server: WireMockServer) {
        server.resetAll()
    }

    fun mockPersonsScenario(server: WireMockServer, persons: List<PersonDto>) {
        resetStubs(server)

        val jsonResponses = persons.map { person ->
            gson.toJson(Persons(results = listOf(person), info = Info("seed", 1, 1, "1.4")))
        }

        WireMock.stubFor(
            get(urlEqualTo(API_PATH))
                .inScenario(SCENARIO_NAME)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponses[0])
                )
                .willSetStateTo("SECOND")
        )
        WireMock.stubFor(
            get(urlEqualTo(API_PATH))
                .inScenario(SCENARIO_NAME)
                .whenScenarioStateIs("SECOND")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponses[1])
                )
                .willSetStateTo("THIRD")
        )
        WireMock.stubFor(
            get(urlEqualTo(API_PATH))
                .inScenario(SCENARIO_NAME)
                .whenScenarioStateIs("THIRD")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponses[2])
                )
        )
    }

    fun mockNetworkError(server: WireMockServer) {
        resetStubs(server)
        WireMock.stubFor(
            get(urlEqualTo(API_PATH))
                .willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE))
        )
    }
}
