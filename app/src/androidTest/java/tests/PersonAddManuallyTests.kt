package tests

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import org.junit.Test
import screens.PersonScreen
import screens.MainScreen

/**
 * @author a.m.sidenov
 */
class PersonAddManuallyTests : BaseTest() {

    @Test
    fun openPersonInfoScreenTest() = run {
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
            clickPersonOnPosition(0)
        }
        with(PersonScreen(this)) {
            checkPersonFields("Edvard", "Wojcik", "М", "1983-09-16"  )
        }
    }

    @Test
    fun editPersonTest() = run {
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
            clickPersonOnPosition(0)
        }
        with(PersonScreen(this)) {
            editPersonName("Иосиф")
            checkPersonName("Иосиф")
        }
    }

    @Test
    fun addPersonManuallyTest() = run {
        with(MainScreen(this)) {
            addPersonManually()
        }
        with(PersonScreen(this)) {
            createPerson()
            clickSaveButton()
        }
        with(MainScreen(this)) {
            checkPersonInfoAtPosition(0, "Иван Иванов", "Male", "asdasd@mail.ru", "+79999999999", "Улица пушкина", "52")
            checkAgeOfPersonAtPosition(0, "24")
        }
    }

    @Test
    fun addPersonManuallyErrorTest() = run {
        with(MainScreen(this)) {
            addPersonManually()
        }
        with(PersonScreen(this)) {
            clickSaveButton()
            checkGenderError()
        }
    }

    @Test
    fun errorTextHideTest() = run {
        with(MainScreen(this)) {
            addPersonManually()
        }
        with(PersonScreen(this)) {
            createPerson()
            editPersonGender("я")
            clickSaveButton()
            checkGenderError()
            clickGenderField()
            editPersonGender("")
            checkGenderErrorNotExist()
        }
    }
}