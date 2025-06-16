package ru.tinkoff.favourite_persons.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import org.hamcrest.Matchers.not
import ru.tinkoff.favourite_persons.utils.TextInputLayoutErrorMatcher.hasTextInputLayoutErrorText
import ru.tinkoff.favouritepersons.R

object DetailsPage : KScreen<DetailsPage>() {
    override val layoutId: Int = R.layout.person_item_activity
    override val viewClass: Class<*> =
        ru.tinkoff.favouritepersons.presentation.activities.PersonItemActivity::class.java

    private val nameField = KEditText { withId(R.id.et_name) }
    private val surnameField = KEditText { withId(R.id.et_surname) }
    private val genderField = KEditText { withId(R.id.et_gender) }
    private val birthDateField = KEditText { withId(R.id.et_birthdate) }
    private val emailField = KEditText { withId(R.id.et_email) }
    private val phoneField = KEditText { withId(R.id.et_phone) }
    private val addressField = KEditText { withId(R.id.et_address) }
    private val scoreField = KEditText { withId(R.id.et_score) }
    private val imageUrlField = KEditText { withId(R.id.et_image) }
    private val saveButton = KButton { withId(R.id.submit_button) }

    fun checkPersonDetails(
        name: String,
        surname: String,
        gender: String,
        birthDate: String
    ) {
        nameField.hasText(name)
        surnameField.hasText(surname)
        genderField.hasText(gender)
        birthDateField.hasText(birthDate)
    }

    fun enterName(name: String) {
        nameField.replaceText(name)
    }

    fun enterSurname(surname: String) {
        surnameField.replaceText(surname)
    }

    fun enterGender(gender: String) {
        genderField.replaceText(gender)
    }

    fun enterBirthDate(birthDate: String) {
        birthDateField.replaceText(birthDate)
    }

    fun enterImageUrl(url: String) {
        imageUrlField.replaceText(url)
    }

    fun enterEmail(email: String) {
        emailField.replaceText(email)
    }

    fun enterPhone(phone: String) {
        phoneField.replaceText(phone)
    }

    fun enterAddress(address: String) {
        addressField.replaceText(address)
    }

    fun enterScore(score: String) {
        scoreField.replaceText(score)
    }

    fun clickSaveButton() {
        saveButton.click()
    }

    fun checkGenderErrorIsDisplayed() {
        onView(withId(R.id.til_gender))
            .check(matches(hasTextInputLayoutErrorText("Поле должно быть заполнено буквами М или Ж")))
    }

    fun checkGenderErrorIsNotDisplayed() {
        onView(withId(R.id.til_gender))
            .check(matches(not(hasTextInputLayoutErrorText("Поле должно быть заполнено буквами М или Ж"))))
    }
}
