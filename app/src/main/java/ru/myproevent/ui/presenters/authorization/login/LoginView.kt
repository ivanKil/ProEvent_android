package ru.myproevent.ui.presenters.authorization.login

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface LoginView : BaseMvpView {
    @OneExecution
    fun finishAuthorization()
}