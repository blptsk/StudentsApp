package ru.tinkoff.favouritepersons.screens

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.helpers.ListOfPerson
import com.google.android.material.R as SnackbarR

/**
 * @author n.miroshkin
 */
class MainScreen(testContext: TestContext<*>) : BaseScreen(testContext) {

    private val addPersonBtn = KButton { withId(R.id.fab_add_person) }
    private val emptyPersonScreen = KTextView { withId(R.id.tw_no_persons) }
    private val addPersonByNetworkBtn = KButton { withId(R.id.fab_add_person_by_network) }
    private val addPersonManuallyBtn = KButton { withId(R.id.fab_add_person_manually) }
    private val personList = KRecyclerView(
        builder = { withId(R.id.rv_person_list) },
        itemTypeBuilder = {
            itemType(::ListOfPerson)
        }
    )
    private val sortBtn = KButton { withId(R.id.action_item_sort) }
    private val snackar = KTextView { withId(SnackbarR.id.snackbar_text) }

    fun clickPersonBtn() {
        addPersonBtn.click()
    }

    fun verifyEmptyScreen() {
        emptyPersonScreen.isDisplayed()
        emptyPersonScreen.hasText(R.string.start_screen_no_persons_text)
    }

    fun verifyScreenIsNotEmpty() {
        emptyPersonScreen.isNotDisplayed()
    }

    fun addPersonByNetwork() {
        clickPersonBtn()
        addPersonByNetworkBtn.click()
    }

    fun clickAddPersonByNetworkBtn() {
        addPersonByNetworkBtn.click()
    }

    fun addPersonByManually() {
        clickPersonBtn()
        addPersonManuallyBtn.click()
    }

    fun removeStudentByIndex(index: Int) {
        personList {
            childAt<ListOfPerson>(index) {
                view.perform(ViewActions.swipeLeft())
            }
        }
    }

    fun clickStudentByIndex(index: Int) {
        personList {
            childAt<ListOfPerson>(index) {
                click()
            }
        }
    }

    fun getElementByAndId(index: Int, elementId: Int) : String {
        var actualData = ""
        personList {
            childAt<ListOfPerson>(index) {
                view.interaction.check { view, _ ->
                    val textView = view.findViewById<TextView>(elementId)
                    actualData = textView?.text?.toString() ?: ""
                }
            }
        }
        return actualData
    }

    fun verifyStudentNameByIndex(index: Int, fullName: String) {
        val actualName = getElementByAndId(index, R.id.person_name)
        assertEquals(fullName, actualName)
    }

    fun verifyGenderAndAgeByIndex(index: Int, gender: String, age: String) {
        val actualName = getElementByAndId(index, R.id.person_private_info)
        when(gender) {
            "М" -> assertEquals("Male, ${age}", actualName)
            "Ж" -> assertEquals("Female, ${age}, ", actualName)
        }
    }

    fun verifyEmaleByIndex(index: Int, emale: String) {
        val actualData = getElementByAndId(index, R.id.person_email)
        assertEquals(emale, actualData)
    }

    fun verifyPhoneByIndex(index: Int, phone: String) {
        val actualData = getElementByAndId(index, R.id.person_phone)
        assertEquals(phone, actualData)
    }

    fun verifyAddressByIndex(index: Int, address: String) {
        val actualData = getElementByAndId(index, R.id.person_address)
        assertEquals(address, actualData)
    }

    fun verifyScoreByIndex(index: Int, score: String) {
        val actualData = getElementByAndId(index, R.id.person_rating)
        assertEquals(score, actualData)
    }

    fun checkStudentAmount(amount: Int) {
        personList.hasSize(amount)
    }

    fun clickSortBnt() {
        sortBtn.click()
    }

    fun verifySorterStudentByAge() {
        KView { withId(R.id.rv_person_list) }.view.interaction.check { view, noViewFoundException ->

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            val ages = mutableListOf<Int>()

            for (i in 0 until (adapter?.itemCount ?: 0)) {
                recyclerView.scrollToPosition(i)
                val holder = recyclerView.findViewHolderForAdapterPosition(i)

                val infoView = holder?.itemView?.findViewById<TextView>(R.id.person_private_info)

                infoView?.let {
                    val text = it.text.toString()
                    val age = text.substringAfter(",")
                        .trim()
                        .filter { char -> char.isDigit() }
                        .toIntOrNull()

                    if (age != null) ages.add(age)
                }
            }
            val descendingAges = ages.sortedDescending()

            assertFalse("Sort student has errors", ages != descendingAges)
        }
    }

    fun checkSnackbar() {
        snackar.isDisplayed()
        snackar.hasText("Internet error! Check your connection")
    }
}
