package ru.myproevent.ui.presenters.events.adapter

import ru.myproevent.ui.presenters.IListPresenter
import ru.myproevent.ui.presenters.events.IEventItemView

interface IEventsListPresenter : IListPresenter<IEventItemView> {
    fun onEditButtonClick(view: IEventItemView)
}
