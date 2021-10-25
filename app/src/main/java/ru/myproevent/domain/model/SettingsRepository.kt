package ru.myproevent.domain.model

import android.content.Context
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log

class EncryptedInfo {
    var data: String? = null
    var iv: ByteArray? = null
}

// Source: https://gist.github.com/JosiasSena/3bf4ca59777f7dedcaf41a495d96d984
object SettingsRepository {
    fun getProperty(key: String, context: Context): EncryptedInfo {
        val info = EncryptedInfo()

        info.data = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(key, null)

        val iv = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("${key}_iv", null)

        info.iv?.let { Base64.decode(it, Base64.DEFAULT) }

        return info
    }

    fun setProperty(key: String, encryptedValue: String, iv: ByteArray, context: Context) {
        val ivString = Base64.encodeToString(iv, Base64.DEFAULT)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        settingPref.putString(key, encryptedValue)
        settingPref.apply()

        val settingIvPref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        settingIvPref.putString("${key}_iv", ivString)
        settingIvPref.apply()

        val encryptedToken = getProperty(key, context)
        Log.d(
            "[MYLOG]", "get iv after setProperty: ${
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("${key}_iv", null)
            }"
        )
    }
}