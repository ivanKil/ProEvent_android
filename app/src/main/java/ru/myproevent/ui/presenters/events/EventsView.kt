package ru.myproevent.ui.presenters.events

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.myproevent.domain.models.entities.Event

@AddToEndSingle
interface EventsView : MvpView {
    fun init()
    fun updateList()
    fun showFilterOptions()
    fun hideFilterOptions()
    fun selectFilterOption(option: Event.Status)
    fun setNoEventsLayoutVisibility(visivle: Boolean)
    @OneExecution
    fun showToast(text: String)
}
