package ru.tinkoff.favourite_persons.tests

import org.junit.Test
import ru.tinkoff.favourite_persons.pages.MainPage
import ru.tinkoff.favourite_persons.pages.SnackbarPage
import ru.tinkoff.favourite_persons.utils.WireMockHelper

class MainPageTests : BaseTest() {

    @Test
    fun emptyListMessageHidingTest() = run {
        step("Add person and verify empty list message is hidden") {
            with(MainPage) {
                clickAddPersonButton()
                clickLoadFromCloudButton()
                checkEmptyListMessageIsNotDisplayed()
            }
        }
    }

    @Test
    fun personDeletionTest() = run {
        step("Add multiple persons and delete one") {
            with(MainPage) {
                clickAddPersonButton()
                loadPersonsFromCloud(3)
                deletePerson()
            }
            assert(MainPage.getPersonListCount() == 2)
        }
    }

    @Test
    fun defaultSortOptionTest() = run {
        step("Check default sort option is selected") {
            with(MainPage) {
                clickSortButton()
                checkDefaultSortOptionIsSelected()
            }
        }
    }

    @Test
    fun sortByAgeTest() = run {
        step("Add persons and sort by age") {
            with(MainPage) {
                clickAddPersonButton()
                loadPersonsFromCloud(3)
                clickSortButton()
                selectSortByAge()
                verifyPersonsOrderByAge()
            }
        }
    }

    @Test
    fun networkErrorShowsSnackbarTest() = run {
        step("Stub network error and verify Snackbar") {
            WireMockHelper.mockNetworkError(wireMockServer)

            with(MainPage) {
                clickAddPersonButton()
                clickLoadFromCloudButton()
            }

            SnackbarPage.checkSnackbarIsDisplayed()
        }
    }
}
