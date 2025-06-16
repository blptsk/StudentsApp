package ru.tinkoff.favourite_persons.utils

import android.content.Context

object BaseUrlHelper {

    fun setBaseUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences("demo_url", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("url", url)
            .apply()
    }


    fun clearBaseUrl(context: Context) {
        val prefs = context.getSharedPreferences("demo_url", Context.MODE_PRIVATE)
        prefs.edit()
            .remove("url")
            .apply()
    }
}