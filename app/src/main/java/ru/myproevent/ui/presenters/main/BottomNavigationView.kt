package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface BottomNavigationView : MvpView {
    fun openTab(tab: Tab)
    fun resetState()
    // Экран, который вызывает эту функцию, должен вызывать showBottomNavigation после того как он закроется.
    // При этом showBottomNavigation желательно вызывать после localRouter.exit().
    // Иначе BottomNavigation может стать для пользователя недоступным
    fun hideBottomNavigation()
    // Если эту функцию вызывает экран, то вызов желательно производить сразу после super.onCreateView.
    // Иначе анимации переходов между экранами могут работать некорректно
    fun showBottomNavigation()
}