package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.SingleState

@SingleState
interface MainView : MvpView{
    fun hideBottomNavigation()
    fun showBottomNavigation()
    fun selectItem(menu: Menu)
}