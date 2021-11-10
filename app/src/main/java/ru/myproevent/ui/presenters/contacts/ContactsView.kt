package ru.myproevent.ui.presenters.contacts

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.domain.model.entities.Status

@StateStrategyType(AddToEndStrategy::class)
interface ContactsView: MvpView{
    fun init()
    fun showToast(text: String)
    fun hideConfirmationScreen()
    fun showConfirmationScreen(action: Contact.Action, callBack: ((confirmed: Boolean) -> Unit)?)
    fun updateList()
}
