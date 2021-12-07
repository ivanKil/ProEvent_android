package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker

import android.os.Bundle
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.ui.presenters.contacts.contacts_list.ContactsView

@AddToEndSingle
interface ParticipantFromContactsPickerView : ContactsView{
    fun setResult(requestKey: String, result: Bundle)
    fun updatePickedContactsList()
    fun showPickedParticipants()
    fun hidePickedParticipants()
    fun setPickedParticipantsCount(curr: Int, all: Int)
}