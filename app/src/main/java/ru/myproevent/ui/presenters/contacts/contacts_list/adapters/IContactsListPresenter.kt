package ru.myproevent.ui.presenters.contacts.contacts_list.adapters

import ru.myproevent.ui.presenters.IListPresenter
import ru.myproevent.ui.presenters.contacts.contacts_list.IContactItemView

interface IContactsListPresenter : IListPresenter<IContactItemView> {
    fun onStatusClick(view: IContactItemView)
}
