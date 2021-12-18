package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_by_email_picker

import android.os.Bundle
import com.github.terrakok.cicerone.Router
import ru.myproevent.domain.utils.CONTACTS_KEY
import ru.myproevent.domain.utils.PARTICIPANTS_PICKER_RESULT_KEY
import ru.myproevent.ui.presenters.BaseMvpPresenter

class ParticipantByEmailPickerPresenter(localRouter: Router) :
    BaseMvpPresenter<ParticipantByEmailPickerView>(localRouter) {
    fun inviteParticipantByEmail(email: String) {
        viewState.setResult(
            PARTICIPANTS_PICKER_RESULT_KEY,
            Bundle().apply { putParcelableArray(CONTACTS_KEY, arrayOf()) }
        )
    }
}