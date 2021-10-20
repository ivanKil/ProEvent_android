package ru.myproevent.ui.presenters.login

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class LoginPresenter : MvpPresenter<LoginView>() {
    @Inject
    lateinit var router: Router
    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun confirmLogin(){
        router.newRootScreen(screens.home())
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}