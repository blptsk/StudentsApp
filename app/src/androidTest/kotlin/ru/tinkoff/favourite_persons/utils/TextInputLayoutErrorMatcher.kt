package ru.tinkoff.favourite_persons.utils

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object TextInputLayoutErrorMatcher {
    fun hasTextInputLayoutErrorText(expectedError: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with TextInputLayout error text: $expectedError")
            }

            override fun matchesSafely(view: View): Boolean {
                if (view !is TextInputLayout) return false
                val error = view.error ?: return false
                return error.toString() == expectedError
            }
        }
    }
}