package ru.myproevent.ui.presenters.settings.settings_list

import com.github.terrakok.cicerone.Router
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class SettingsPresenter(localRouter: Router) : BaseMvpPresenter<SettingsView>(localRouter) {
    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    fun account() {
        localRouter.navigateTo(screens.account())
    }

    fun security() {
        localRouter.navigateTo(screens.security())
    }

    fun logout() {
        loginRepository.logoutFromThisDevice()
        localRouter.newRootScreen(screens.authorization())
    }
}