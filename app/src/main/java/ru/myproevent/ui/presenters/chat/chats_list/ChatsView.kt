package ru.myproevent.ui.presenters.chat.chats_list

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface ChatsView : BaseMvpView {
    fun init()
    fun updateList()
    fun showFilterOptions()
    fun hideFilterOptions()
    fun selectFilterOption(option: Event.Status)
    fun setNoEventsLayoutVisibility(visivle: Boolean)
}
