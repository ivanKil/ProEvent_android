package ru.myproevent.ui.presenters.events.event.participant_pickers

import android.os.Bundle
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface ParticipantsPickerView : BaseMvpView {
    fun setResult(requestKey: String, result: Bundle)
}