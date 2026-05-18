package ru.tinkoff.favouritepersons.data.network

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun getBaseUrlForService(context: Context) : String{
        val mPrefs = context.getSharedPreferences("demo_url", AppCompatActivity.MODE_PRIVATE)
        var data :String? = null
        mPrefs?.let {
            val prefsEditor = mPrefs.edit()
            data = mPrefs.getString("url", null)
            prefsEditor.commit()
        }

        return data ?: "https://randomuser.me/"
}
