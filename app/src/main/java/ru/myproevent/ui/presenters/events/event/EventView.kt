package ru.myproevent.ui.presenters.events.event

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import ru.myproevent.domain.models.ProfileDto

@AddToEnd
interface EventView : MvpView {
    fun showMessage(message: String)
    fun addParticipantItemView(profileDto: ProfileDto)
    fun clearParticipants()
}