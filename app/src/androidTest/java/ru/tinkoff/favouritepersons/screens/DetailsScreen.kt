package ru.tinkoff.favouritepersons.screens

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.tinkoff.favouritepersons.R
import ru.tinkoff.favouritepersons.presentation.PersonErrorMessages

/**
 * @author n.miroshkin
 */
class DetailsScreen(testContext: TestContext<*>) : BaseScreen(testContext) {

    val screenTitle = KTextView { withId(R.id.tw_person_screen_title) }
    val firstName = KEditText { withId(R.id.et_name) }
    val lastName = KEditText { withId(R.id.et_surname) }
    val gender = KEditText { withId(R.id.et_gender) }
    val birthday = KEditText { withId(R.id.et_birthdate) }
    val saveBnt = KButton { withId(R.id.submit_button) }
    val emale = KEditText { withId(R.id.et_email) }
    val phone = KEditText { withId(R.id.et_phone) }
    val address = KEditText { withId(R.id.et_address) }
    val photoLink = KEditText { withId(R.id.et_image) }
    val score = KEditText { withId(R.id.et_score) }
    val fieldError = KTextInputLayout {withId(R.id.til_gender) }

    fun verifyScreenOpened() {
        screenTitle.isDisplayed()
    }

    fun verifyFirstName(name: String) {
        firstName.hasText(name)
    }

    fun verifyLastName(lastName: String) {
        this.lastName.hasText(lastName)
    }

    fun verifyGender(gender: String) {
        when(gender) {
            "f" -> this.gender.hasText("Ж")
            "m" -> this.gender.hasText("М")
        }
    }

    fun verifyBirthday(birthday: String) {
        this.birthday.hasText(birthday)
    }

    fun inputFirstName(firstName: String) {
        this.firstName.replaceText(firstName)
    }

    fun inputLastName(lastName: String) {
        this.lastName.replaceText(lastName)
    }

    fun inputGender(gender: String) {
        this.gender.replaceText(gender)
    }

    fun clearFieldGender() {
        gender.clearText()
    }

    fun inputBirthday(birthday: String) {
        this.birthday.replaceText(birthday)
    }

    fun inputMale(male: String) {
        this.emale.replaceText(male)
    }

    fun inputPhone(phone: String) {
        this.phone.replaceText(phone)
    }

    fun inputAddress(address: String) {
        this.address.replaceText(address)
    }

    fun inputPhotoLink(photoLink: String) {
        this.photoLink.replaceText(photoLink)
    }

    fun inputScore(score: String) {
        this.score.replaceText(score)
    }

    fun clickSubmitBtn() {
        saveBnt.click()
    }

    fun checkError() {
        fieldError.hasError(PersonErrorMessages.GENDER.errorMessage)
    }
    fun errorNotDisplayed() {
        fieldError.hasNoError()
    }
}
