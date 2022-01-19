package ru.myproevent.ui.presenters.chat.chats_list

import ru.myproevent.ui.presenters.IItemView

interface IChatItemView : IItemView {
    fun setName(name: String)
    fun setTime(time: String)
    fun loadImg(url: String)
}