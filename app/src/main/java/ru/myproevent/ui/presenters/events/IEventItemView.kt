package ru.myproevent.ui.presenters.events

import ru.myproevent.domain.models.entities.Status
import ru.myproevent.ui.presenters.IItemView

interface IEventItemView : IItemView {
    fun setName(name: String)
    fun setDescription(description: String)
    fun loadImg(url: String)
    fun setStatus(status: Status)
}