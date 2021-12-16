package ru.myproevent.ui.presenters.events.event

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEnd
interface EventView : BaseMvpView {
    fun addParticipantItemView(profileDto: ProfileDto)
    fun clearParticipants()
}