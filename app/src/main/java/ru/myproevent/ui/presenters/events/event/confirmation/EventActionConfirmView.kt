package ru.myproevent.ui.presenters.events.event.confirmation

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface EventActionConfirmView : MvpView{
    fun showMessage(message: String)
}