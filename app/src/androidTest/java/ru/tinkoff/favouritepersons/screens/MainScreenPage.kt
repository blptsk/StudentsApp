package ru.tinkoff.favouritepersons.screens

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.snackbar.SnackbarContentLayout
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.switch.KSwitch
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KSnackbar
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.utils.StudentData


class MainScreenPage : BaseScreen() {
    val addPersonButton = KButton { withId(R.id.fab_add_person) }
    val addRandomPersonButton = KButton { withId(R.id.fab_add_person_by_network) }
    val addPersonManuallyButton = KButton { withId(R.id.fab_add_person_manually) }
    val sortButton = KButton { withId(R.id.action_item_sort) }
    val snackbarText = KTextView {
        isInstanceOf(AppCompatTextView::class.java)
        withParent { isInstanceOf(SnackbarContentLayout::class.java) }
    }
    val snackbar = KSnackbar()
    val sortDefault = KSwitch { withId(R.id.bsd_rb_default) }
    val sortAge = KSwitch { withId(R.id.bsd_rb_age) }
    val noPersonsTitle = KTextView { withId(R.id.tw_no_persons) }

    val studentCard = KRecyclerView(
        { withId(R.id.rv_person_list) },
        itemTypeBuilder = {
            itemType(::CardView)
        })

    class CardView(parent: Matcher<View>) : KRecyclerItem<CardView>(parent) {
        val name = KTextView(parent) { withId(R.id.person_name) }
        val privateInfo = KTextView(parent) { withId(R.id.person_private_info) }
        val email = KTextView(parent) { withId(R.id.person_email) }
        val phone = KButton(parent) { withId(R.id.person_phone) }
        val address = KButton(parent) { withId(R.id.person_address) }
        val rating = KButton(parent) { withId(R.id.person_rating) }
    }

    fun deleteStudent() {
        studentCard { swipeLeft() }
    }

    fun checkPrivateInfoText(cardPosition: Int, expectedText: String) {
        studentCard {
            childAt<MainScreenPage.CardView>(cardPosition) {
                privateInfo.hasText(expectedText)
            }
        }
    }

    fun checkName(cardPosition: Int, expectedName: String, expectedSurname: String) {
        studentCard {
            childAt<MainScreenPage.CardView>(cardPosition) {
                name {
                    hasText("$expectedName $expectedSurname")
                }
            }
        }
    }

    fun checkSnackbarIsDisplayed() {
        snackbar.inRoot { isDisplayed() }
    }

    fun checkStudentCard(cardPosition: Int, studentInfo: StudentData, age: Int) {
        studentCard {
            childAt<MainScreenPage.CardView>(cardPosition) {
                name.hasText("${studentInfo.name} ${studentInfo.surname}")
                privateInfo.hasText("Male, $age")
                email.hasText(studentInfo.email)
                phone.hasText(studentInfo.phone)
                address.hasText(studentInfo.address)
                rating.hasText(studentInfo.score)
            }
        }
    }

    companion object {
        inline operator fun invoke(crossinline block: MainScreenPage.() -> Unit) =
            MainScreenPage().block()
    }
}