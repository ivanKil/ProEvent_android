package ru.myproevent.ui.presenters.settings

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class SettingsPresenter : MvpPresenter<SettingsView>() {
    @Inject
    lateinit var router: Router
    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}