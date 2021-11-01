package ru.myproevent.ui.presenters.security

import android.widget.Toast
import ru.myproevent.ProEventApp
import ru.myproevent.ui.presenters.BaseMvpPresenter

class SecurityPresenter : BaseMvpPresenter<SecurityView>() {
    fun save(email: String, login: String, password: String) {
        Toast.makeText(
            ProEventApp.instance.applicationContext,
            "saved: $email, $login, $password",
            Toast.LENGTH_SHORT
        ).show()
    }
}