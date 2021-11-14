package ru.myproevent.ui.presenters.contacts

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.myproevent.domain.model.entities.Contact

@AddToEndSingle
interface ContactsView : MvpView {
    fun init()
    @OneExecution
    fun showToast(text: String)
    fun hideConfirmationScreen()
    fun showConfirmationScreen(action: Contact.Action, callBack: ((confirmed: Boolean) -> Unit)?)
    fun updateList()
}
