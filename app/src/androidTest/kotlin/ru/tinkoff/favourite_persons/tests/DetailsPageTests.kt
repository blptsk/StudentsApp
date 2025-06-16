package ru.tinkoff.favourite_persons.tests

import org.junit.Test
import ru.tinkoff.favourite_persons.pages.DetailsPage
import ru.tinkoff.favourite_persons.pages.MainPage

class DetailsPageTests : BaseTest() {
    private val newName = "Иосиф"
    private val expectedPerson get() = mockedPersons[0]
    private val expectedGender
        get() = when (expectedPerson.gender) {
            "male" -> "М"
            "female" -> "Ж"
            else -> expectedPerson.gender
        }

    @Test
    fun personDetailsDisplayTest() = run {
        step("Add person") {
            with(MainPage) {
                clickAddPersonButton()
                loadPersonsFromCloud(1)
                clickOnPerson(0)
            }
        }

        step("Check person details") {
            DetailsPage.checkPersonDetails(
                name = expectedPerson.name.first,
                surname = expectedPerson.name.last,
                gender = expectedGender,
                birthDate = expectedPerson.dob.date.substring(0, 10)
            )
        }
    }

    @Test
    fun personEditTest() = run {
        step("Edit person name") {
            with(MainPage) {
                clickAddPersonButton()
                loadPersonsFromCloud(1)
                clickOnPerson(0)
            }
            with(DetailsPage) {
                enterName(newName)
                clickSaveButton()
            }
        }

        step("Check person name is changed") {
            MainPage.checkPersonName("$newName ${expectedPerson.name.last}")
        }
    }

    @Test
    fun addPersonManuallyTest() = run {
        step("Add person manually") {
            with(MainPage) {
                clickAddPersonButton()
                clickAddManuallyButton()
            }

            with(DetailsPage) {
                enterName(expectedPerson.name.first)
                enterSurname(expectedPerson.name.last)
                enterGender(expectedGender)
                enterEmail(expectedPerson.email)
                enterPhone(expectedPerson.phone)
                enterAddress("${expectedPerson.location.state} ,${expectedPerson.location.city},${expectedPerson.location.street}")
                enterBirthDate(expectedPerson.dob.date.substring(0, 10))
                enterImageUrl("https://example.com/image.jpg")
                enterScore("100")
                clickSaveButton()
            }
        }
        step("Check person is added") {
            MainPage.checkPersonDisplayed(expectedPerson)
        }
    }

    @Test
    fun emptyGenderFieldErrorTest() = run {
        step("Verify error message for empty fields") {
            with(MainPage) {
                clickAddPersonButton()
                clickAddManuallyButton()
            }

            with(DetailsPage) {
                clickSaveButton()
                checkGenderErrorIsDisplayed()
            }
        }
    }

    @Test
    fun errorMessageHidingTest() = run {
        step("Verify error message disappears after input") {
            with(MainPage) {
                clickAddPersonButton()
                clickAddManuallyButton()
            }

            with(DetailsPage) {
                enterName(expectedPerson.name.first)
                enterSurname(expectedPerson.name.last)
                enterGender("У")
                enterEmail(expectedPerson.email)
                enterPhone(expectedPerson.phone)
                enterAddress("${expectedPerson.location.state} ,${expectedPerson.location.city},${expectedPerson.location.street}")
                enterBirthDate(expectedPerson.dob.date.substring(0, 10))
                enterImageUrl("https://example.com/image.jpg")
                enterScore("100")
                clickSaveButton()

                checkGenderErrorIsDisplayed()

                enterGender(expectedGender)
                checkGenderErrorIsNotDisplayed()

                clickSaveButton()
            }
        }
    }
}
