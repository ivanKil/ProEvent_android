package ru.myproevent.ui.presenters.events

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface EventsView : MvpView {
    fun init()
    fun updateList()

    @OneExecution
    fun showToast(text: String)
}
