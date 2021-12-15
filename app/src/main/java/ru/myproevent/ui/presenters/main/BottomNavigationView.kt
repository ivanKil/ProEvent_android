package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface BottomNavigationView : MvpView {
    fun openTab(tab: Tab)
    fun resetState()
}