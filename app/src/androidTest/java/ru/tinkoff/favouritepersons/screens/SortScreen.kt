package ru.tinkoff.favouritepersons.screens

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import io.github.kakaocup.kakao.check.KCheckBox
import io.github.kakaocup.kakao.common.views.KView
import ru.tinkoff.favouritepersons.R

/**
 * @author n.miroshkin
 */
class SortScreen(testContext: TestContext<*>) : BaseScreen(testContext) {
    val sortPanel = KView { withId(R.id.bottom_sheet) }
    val defaultPoint = KCheckBox { withId(R.id.bsd_rb_default)}
    val byAgePoint = KCheckBox { withId(R.id.bsd_rb_age)}

    fun verifySortPanelOpened() {
        sortPanel.isDisplayed()
    }

    fun verifyDefaultPontIsActive() {
        defaultPoint.isChecked()
    }

    fun clickAgePont() {
        byAgePoint.click()
    }

    fun verifyAgePontIsActive() {
        byAgePoint.isChecked()
    }

}
