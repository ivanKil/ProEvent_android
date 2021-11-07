package ru.myproevent.ui.adapters.contacts

import ru.myproevent.domain.model.entities.Status
import ru.myproevent.ui.adapters.IItemView

interface IContactItemView : IItemView {
    fun setName(name: String)
    fun setDescription(description: String)
    fun loadImg(url: String)
    fun setStatus(status: Status)
}