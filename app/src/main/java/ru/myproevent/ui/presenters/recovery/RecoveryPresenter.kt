package ru.myproevent.ui.presenters.recovery

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class RecoveryPresenter : MvpPresenter<RecoveryView>() {
    @Inject
    lateinit var router: Router

    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}