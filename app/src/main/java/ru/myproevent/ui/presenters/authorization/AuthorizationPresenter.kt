package ru.myproevent.ui.presenters.authorization

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class AuthorizationPresenter @Inject constructor(
    private var router: Router,
) : MvpPresenter<AuthorizationView>() {
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
            router.replaceScreen(screens.home())
        } ?: run { viewState.authorizationDataInvalid() }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}