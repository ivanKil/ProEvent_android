package ru.myproevent.ui.presenters.account

import android.widget.Toast
import ru.myproevent.ProEventApp
import ru.myproevent.ui.presenters.BaseMvpPresenter

class AccountPresenter : BaseMvpPresenter<AccountView>() {
    fun save(
        name: String,
        phone: String,
        dateOfBirth: String,
        position: String,
        role: String
    ) {
        // TODO
        Toast.makeText(
            ProEventApp.instance.applicationContext,
            "saved:$name, $phone, $dateOfBirth, $position, $role",
            Toast.LENGTH_SHORT
        ).show()
    }
}