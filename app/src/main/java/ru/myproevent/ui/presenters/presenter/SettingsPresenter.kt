package ru.myproevent.ui.presenters.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.presenters.authorization.AuthorizationView
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private var router: Router,
) : MvpPresenter<SettingsView>() {
    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}