package ru.myproevent.ui.presenters.events.event.participant_pickers

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface ParticipantPickerTypeSelectionView : MvpView {
    fun showMessage(message: String)
}