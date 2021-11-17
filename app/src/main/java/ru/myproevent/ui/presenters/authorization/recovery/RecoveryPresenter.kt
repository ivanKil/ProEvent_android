package ru.myproevent.ui.presenters.authorization.recovery

import com.github.terrakok.cicerone.Router
import ru.myproevent.ui.presenters.BaseMvpPresenter

class RecoveryPresenter(localRouter: Router) : BaseMvpPresenter<RecoveryView>(localRouter) {
    fun authorize(){
        localRouter.navigateTo(screens.authorization())
    }
}