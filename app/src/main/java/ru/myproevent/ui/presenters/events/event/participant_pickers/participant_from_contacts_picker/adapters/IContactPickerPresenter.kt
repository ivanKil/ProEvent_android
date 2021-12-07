package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker.adapters

import ru.myproevent.ui.presenters.IListPresenter
import ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker.IContactPickerItemView

interface IContactPickerPresenter : IListPresenter<IContactPickerItemView> {
    fun onStatusClick(view: IContactPickerItemView)
}