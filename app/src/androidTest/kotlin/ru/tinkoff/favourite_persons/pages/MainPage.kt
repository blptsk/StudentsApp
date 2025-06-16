package ru.tinkoff.favourite_persons.pages

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.data.network.PersonDto

object MainPage : KScreen<MainPage>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewClass: Class<*> = ru.tinkoff.favouritepersons.presentation.activities.MainActivity::class.java

    private val emptyListMessage = KTextView { withId(R.id.tw_no_persons) }
    private val personList = KRecyclerView(
        { withId(R.id.rv_person_list) },
        { itemType(::PersonMatcherDto) }
    )

    class PersonMatcherDto(matcher: Matcher<View>) : KRecyclerItem<PersonMatcherDto>(matcher) {
        val privateInfo = KTextView(matcher) { withId(R.id.person_private_info) }
    }

    private val addPersonButton = KButton { withId(R.id.fab_add_person) }
    private val loadFromCloudButton = KButton { withId(R.id.fab_add_person_by_network) }
    private val addManuallyButton = KButton { withId(R.id.fab_add_person_manually) }
    private val sortButton = KButton { withId(R.id.action_item_sort) }
    private val sortDefaultOption = KTextView { withText("По умолчанию") }
    private val sortByAgeOption = KTextView { withText("По возрасту") }

    fun clickAddPersonButton() {
        addPersonButton.click()
    }

    fun clickLoadFromCloudButton() {
        loadFromCloudButton.click()
    }

    fun clickAddManuallyButton() {
        addManuallyButton.click()
    }

    fun checkEmptyListMessageIsNotDisplayed() {
        emptyListMessage.isInvisible()
    }

    fun loadPersonsFromCloud(times: Int) {
        repeat(times) { clickLoadFromCloudButton() }
    }

    fun clickOnPerson(position: Int) {
        personList { childAt<PersonMatcherDto>(position) { click() } }
    }

    fun deletePerson() {
        personList { childAt<PersonMatcherDto>(0) { this@personList.swipeLeft() } }
    }

    fun clickSortButton() {
        sortButton.click()
    }

    fun checkDefaultSortOptionIsSelected() {
        sortDefaultOption { isChecked() }
    }

    fun selectSortByAge() {
        sortByAgeOption.click()
    }

    fun verifyPersonsOrderByAge() {
        personList.isVisible()
        val ages = mutableListOf<Int>()

        personList {
            val count = getSize()
            for (i in 0 until count) {
                childAt<PersonMatcherDto>(i) {
                    var text = ""
                    privateInfo.view.check { view, _ ->
                        val tv = view as TextView
                        text = tv.text.toString()
                    }

                    val age = "\\d+".toRegex().find(text)?.value?.toIntOrNull()
                    age?.let { ages.add(it) }
                }
            }
        }

        assert(ages == ages.sortedDescending()) {
            "Список не отсортирован по возрасту: $ages"
        }
    }

    fun checkPersonName(name: String) {
        KTextView { withText(name) }.isVisible()
    }

    fun checkPersonDisplayed(person: PersonDto) {
        val fullName = "${person.name.first} ${person.name.last}"

        KTextView { withId(R.id.person_name) }
            .hasText(fullName)

        KTextView { withId(R.id.person_private_info) }
            .hasText("${person.gender.capitalize()}, ${person.dob.age}")

        KTextView { withId(R.id.person_email) }
            .hasText(person.email)

        KTextView { withId(R.id.person_phone) }
            .hasText(person.phone)

        KTextView { withId(R.id.person_address) }
            .hasText("${person.location.state} ,${person.location.city},${person.location.street}")

        KTextView { withId(R.id.person_rating) }
          .hasText("100")
    }

    fun getPersonListCount(): Int {
        return personList.getSize()
    }
}

