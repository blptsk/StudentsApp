package ru.tinkoff.favourite_persons.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tomakehurst.wiremock.WireMockServer
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import ru.tinkoff.favourite_persons.utils.BaseUrlHelper
import ru.tinkoff.favourite_persons.utils.FakePersonFactory
import ru.tinkoff.favourite_persons.utils.TestDatabaseHelper
import ru.tinkoff.favourite_persons.utils.WireMockHelper
import ru.tinkoff.favouritepersons.data.network.PersonDto
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity

abstract class BaseTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    protected lateinit var mockedPersons: List<PersonDto>
    private val databaseHelper = TestDatabaseHelper
    protected lateinit var wireMockServer: WireMockServer

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupBeforeAllTests() {
            BaseUrlHelper.setBaseUrl(
                InstrumentationRegistry.getInstrumentation().targetContext,
                "http://localhost:5000"
            )
        }

        @JvmStatic
        @AfterClass
        fun teardownAfterAllTests() {
            BaseUrlHelper.clearBaseUrl(InstrumentationRegistry.getInstrumentation().targetContext)
        }
    }

    @Before
    fun setup() {
        mockedPersons = listOf(
            FakePersonFactory.createPerson(),
            FakePersonFactory.createPerson(),
            FakePersonFactory.createPerson()
        )
        wireMockServer = WireMockHelper.createServer()
        WireMockHelper.mockPersonsScenario(wireMockServer, mockedPersons)
        databaseHelper.setup()
    }

    @After
    fun cleanup() {
        databaseHelper.clearDatabase()
        WireMockHelper.stopServer(wireMockServer)
    }
}
