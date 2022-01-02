package ru.myproevent.ui.presenters.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

/**
 * Функции этого интерфейса должны вызываться исключительно из методов BottomNavigationPresenter.
 * Для реализации этой инкапсуляции в качесве параметра передаётся BottomNavigationPresenterFriendAccess,
 * экземпляр которого может иметь только BottomNavigationPresenter
 */
@AddToEnd
interface BottomNavigationView : MvpView {
    fun showTab(
        tab: Tab,
        friendAccess: BottomNavigationPresenter.BottomNavigationPresenterFriendAccess
    )

    fun resetState(friendAccess: BottomNavigationPresenter.BottomNavigationPresenterFriendAccess)
}