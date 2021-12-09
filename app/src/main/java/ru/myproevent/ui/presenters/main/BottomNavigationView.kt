package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface BottomNavigationView : MvpView {
    fun hideBottomNavigation()
    fun showBottomNavigation()
    fun openTab(tab: Tab)
    fun checkTab(tab: Tab)
}