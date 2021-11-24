package ru.myproevent.ui.presenters.events

import ru.myproevent.ui.presenters.IItemView

interface IEventItemView : IItemView {
    fun setName(name: String)
    fun setTime(time: String)
    fun loadImg(url: String)
}