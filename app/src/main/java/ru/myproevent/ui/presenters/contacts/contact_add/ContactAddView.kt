package ru.myproevent.ui.presenters.contacts.contact_add

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface ContactAddView : BaseMvpView {
    fun showInvitationForm()
    fun showSearchForm()
}