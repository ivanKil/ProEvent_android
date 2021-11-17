package ru.myproevent.ui.presenters.contacts.contact_add

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface ContactAddView : MvpView {
    fun showInvitationForm()
    fun showSearchForm()
}