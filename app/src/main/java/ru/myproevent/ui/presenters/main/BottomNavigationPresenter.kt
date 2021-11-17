package ru.myproevent.ui.presenters.main

import com.github.terrakok.cicerone.Router
import moxy.InjectViewState
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

@InjectViewState
class BottomNavigationPresenter(localRouter: Router) : BaseMvpPresenter<BottomNavigationView>(localRouter) {
    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (loginRepository.getLocalToken() == null) {
            viewState.openTab(Tab.AUTHORIZATION)
        } else {
            viewState.openTab(Tab.HOME)
        }
    }
}