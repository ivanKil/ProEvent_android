package ru.myproevent.domain.models.repositories.local_proevent_user_token

import android.content.Context
import ru.myproevent.ProEventApp
import javax.inject.Inject

class TokenLocalRepository @Inject constructor() : ITokenLocalRepository {
    private val localTokenAlias = "ru.proevent_USER_TOKEN"

    // TODO: SharedPreferences иногда ведёт себя не предсказуемо, может терять значения после закрытия приложения.
    //              Но при этом вновь начинает работать если перезагрузить телефон.
    //              Ещё задеприкейченный EncryptedSharedPreferences мог забыть удалить себя после удаления приложения,
    //              не нашёл информации был ли исправлен этот баг в текущей SharedPreferences, поэтому возможно
    //              стоит как-то динамически генерировать имя файла, чтобы оно было уникальным для каждой установки приложения
    val sharedPref = ProEventApp.instance.applicationContext.getSharedPreferences(
        "ru.proevent_PREFERENCE_FILE", Context.MODE_PRIVATE
    )

    private val editor = sharedPref.edit()

    override fun saveTokenInLocalStorage(token: String) {
        editor.putString(localTokenAlias, token)
        editor.commit()
    }

    override fun getTokenOrNull() = sharedPref.getString(localTokenAlias, null)


    override fun removeTokenFromLocalStorage() {
        editor.remove(localTokenAlias)
        editor.apply()
    }
}