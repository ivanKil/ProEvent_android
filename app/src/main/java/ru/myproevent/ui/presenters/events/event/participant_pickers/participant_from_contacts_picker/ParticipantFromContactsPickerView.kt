package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.ui.presenters.contacts.contacts_list.ContactsView
import ru.myproevent.ui.presenters.events.event.participant_pickers.ParticipantsPickerView

@AddToEndSingle
interface ParticipantFromContactsPickerView : ContactsView, ParticipantsPickerView {
    fun updatePickedContactsList()
    fun showPickedParticipants()
    fun hidePickedParticipants()
    fun setPickedParticipantsCount(curr: Int, all: Int)
    fun showToast(text: String)
}