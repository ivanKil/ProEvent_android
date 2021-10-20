package ru.myproevent.ui.presenters.authorization

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class AuthorizationPresenter: MvpPresenter<AuthorizationView>() {
    @Inject
    lateinit var router: Router

    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    var tries = 0

    fun authorize(login: String, password: String) {
        fun repositoryGetKey(login: String, password: String): String? {
            return if (tries > 1) {
                "apikey"
            } else {
                tries++
                null
            }
        }
        repositoryGetKey(login, password)?.let {
            router.newRootScreen(screens.home())
        } ?: run { viewState.authorizationDataInvalid() }
    }

    fun openRegistration() {
        router.navigateTo(screens.registration())
    }

    fun recoverPassword() {
        router.navigateTo(screens.recovery())
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}