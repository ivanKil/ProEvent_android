package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BottomNavigationView : MvpView{
    fun hideBottomNavigation()
    fun showBottomNavigation()
    fun openTab(tab: Tab)
    fun checkTab(tab: Tab)
}