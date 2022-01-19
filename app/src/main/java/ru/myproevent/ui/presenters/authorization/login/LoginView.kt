package ru.myproevent.ui.presenters.authorization.login

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface LoginView : BaseMvpView {
    fun finishAuthorization()
}