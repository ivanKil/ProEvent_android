package ru.myproevent.ui.presenters.login

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

// TODO: возможно стоит выбрать другую стратегию
@StateStrategyType(AddToEndStrategy::class)
interface LoginView: MvpView