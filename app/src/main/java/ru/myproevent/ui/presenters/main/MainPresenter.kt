package ru.myproevent.ui.presenters.main

import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class MainPresenter : BaseMvpPresenter<MainView>() {
    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (loginRepository.getLocalToken() == null) {
            router.replaceScreen(screens.authorization())
        } else {
            router.replaceScreen(screens.home())
        }
    }

    fun openScreen(screen: Menu) = router.navigateTo(
        when (screen) {
            Menu.HOME -> screens.home()
            Menu.CONTACTS -> screens.contacts()
            Menu.CHAT -> screens.chat()
            Menu.EVENTS -> screens.events()
            Menu.SETTINGS -> screens.settings()
        }
    )


}