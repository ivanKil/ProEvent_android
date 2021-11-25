package ru.myproevent.ui.presenters.events.event

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface EventView : MvpView{
    fun showMessage(message: String)
}