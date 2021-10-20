package ru.myproevent.ui.presenters.code

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class CodePresenter : MvpPresenter<CodeView>() {
    @Inject
    lateinit var router: Router

    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun continueRegistration(){
        router.newRootScreen(screens.login())
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}