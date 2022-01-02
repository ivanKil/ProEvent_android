package ru.myproevent.ui.presenters.events.event.participant_pickers

import com.github.terrakok.cicerone.Router
import ru.myproevent.ui.presenters.BaseMvpPresenter

class ParticipantPickerTypeSelectionPresenter(localRouter: Router) :
    BaseMvpPresenter<ParticipantPickerTypeSelectionView>(localRouter) {
    fun pickFromContacts(pickedParticipantsIds: List<Long>) {
        localRouter.navigateTo(screens.participantFromContactsPicker(pickedParticipantsIds))
    }

    fun pickByEmail() {
        localRouter.navigateTo(screens.participantByEmailPicker())
    }
}