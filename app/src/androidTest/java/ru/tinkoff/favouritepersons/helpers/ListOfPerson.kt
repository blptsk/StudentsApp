package ru.tinkoff.favouritepersons.helpers

import android.view.View
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import org.hamcrest.Matcher

/**
 * @author n.miroshkin
 */
class ListOfPerson(matcher: Matcher<View>) : KRecyclerItem<ListOfPerson>(matcher)
