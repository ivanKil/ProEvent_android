package ru.myproevent.ui.presenters.recovery

import ru.myproevent.ui.presenters.BaseMvpPresenter

class RecoveryPresenter : BaseMvpPresenter<RecoveryView>() {
    fun authorize(){
        router.navigateTo(screens.authorization())
    }
}