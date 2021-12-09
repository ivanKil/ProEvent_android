package ru.myproevent.ui.presenters.contacts.contacts_list

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface ContactsView : BaseMvpView {
    fun init()
    fun hideConfirmationScreen()
    fun showConfirmationScreen(action: Contact.Action, callBack: ((confirmed: Boolean) -> Unit)?)
    fun updateList()
}
