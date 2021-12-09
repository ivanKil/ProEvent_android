package ru.myproevent.ui.presenters

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface BaseMvpView : MvpView {
    @OneExecution
    fun showMessage(text: String)
}