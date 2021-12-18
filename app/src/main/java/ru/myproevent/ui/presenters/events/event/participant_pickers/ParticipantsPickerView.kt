package ru.myproevent.ui.presenters.events.event.participant_pickers

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface ParticipantsPickerView : MvpView {
    fun setResult(requestKey: String, result: Bundle)
}