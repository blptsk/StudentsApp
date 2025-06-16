package ru.tinkoff.favourite_persons.pages

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView

object SnackbarPage : KScreen<SnackbarPage>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    private val snackbarText = KTextView { withId(com.google.android.material.R.id.snackbar_text) }

    fun checkSnackbarIsDisplayed() {
        snackbarText {
            isVisible()
            hasText("Internet error! Check your connection")
        }
    }
}
