package ru.myproevent.ui.presenters.contacts

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.myproevent.domain.model.ProfileDto

@StateStrategyType(AddToEndStrategy::class)
interface ContactsView: MvpView{
    fun init()
    fun updateList()
}
