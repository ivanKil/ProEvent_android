package ru.myproevent.ui.presenters.home

import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class HomePresenter : BaseMvpPresenter<HomeView>() {
    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    fun getToken(): String? = loginRepository.getLocalToken()

    fun logout(){
        loginRepository.logoutFromThisDevice()
        router.newRootScreen(screens.authorization())
    }
}