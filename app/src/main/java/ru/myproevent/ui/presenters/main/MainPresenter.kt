//package ru.myproevent.ui.presenters.main
//
//import com.github.terrakok.cicerone.Router
//import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
//import ru.myproevent.ui.presenters.BaseMvpPresenter
//import javax.inject.Inject
//
//class MainPresenter(localRouter: Router) : BaseMvpPresenter<BottomNavigationView>(localRouter) {
//    @Inject
//    lateinit var loginRepository: IProEventLoginRepository
//
//    override fun onFirstViewAttach() {
//        super.onFirstViewAttach()
//        if (loginRepository.getLocalToken() == null) {
//            localRouter.replaceScreen(screens.authorization())
//        } else {
//            localRouter.replaceScreen(screens.home())
//        }
//    }
//
//    fun openScreen(screen: Tabs) = localRouter.navigateTo(
//        when (screen) {
//            Tabs.HOME -> screens.home()
//            Tabs.CONTACTS -> screens.contacts()
//            Tabs.CHAT -> screens.chat()
//            Tabs.EVENTS -> screens.events()
//            Tabs.SETTINGS -> screens.settings()
//        }
//    )
//}