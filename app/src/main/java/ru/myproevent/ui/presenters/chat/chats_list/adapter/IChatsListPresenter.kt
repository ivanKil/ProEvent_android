package ru.myproevent.ui.presenters.events.adapter

import ru.myproevent.ui.presenters.IListPresenter
import ru.myproevent.ui.presenters.chat.chats_list.IChatItemView

interface IChatsListPresenter : IListPresenter<IChatItemView> {
    fun onEditButtonClick(view: IChatItemView)
}
