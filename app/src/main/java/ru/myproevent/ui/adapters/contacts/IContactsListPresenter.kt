package ru.myproevent.ui.adapters.contacts

import ru.myproevent.ui.adapters.IListPresenter
import ru.myproevent.ui.presenters.contacts.IContactItemView

interface IContactsListPresenter : IListPresenter<IContactItemView>{
    fun onStatusClick(view: IContactItemView)
}
