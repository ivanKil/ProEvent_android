package ru.myproevent.ui.presenters.events.confirmation

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface EventActionConfirmView : MvpView{
    fun showMessage(message: String)
}