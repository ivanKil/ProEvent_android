package ru.myproevent.ui.presenters.authorization.authorization

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

// TODO: возможно стоит выбрать другую стратегию
@StateStrategyType(AddToEndStrategy::class)
interface AuthorizationView: MvpView{
    fun authorizationDataInvalid()
    fun finishAuthorization()
}