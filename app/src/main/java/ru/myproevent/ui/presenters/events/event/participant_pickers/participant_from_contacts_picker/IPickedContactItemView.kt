package ru.myproevent.ui.presenters.events.event.participant_pickers.participant_from_contacts_picker

import ru.myproevent.ui.presenters.IItemView

interface IPickedContactItemView : IItemView {
    fun setName(name: String)
    fun loadImg(url: String)
}