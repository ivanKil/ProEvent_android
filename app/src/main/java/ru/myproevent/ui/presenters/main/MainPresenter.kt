package ru.myproevent.ui.presenters.main

import android.widget.Toast
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private var router: Router,
) : MvpPresenter<MainView>() {
    // TODO: Вынести в Dagger
    private var screens: IScreens = Screens()

    private var currActiveMenu = Menu.HOME

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.authorization())
    }

    fun itemSelected(menu: Menu) {
        currActiveMenu = menu
    }

    fun openHome() {
        if (currActiveMenu == Menu.HOME) {
            return
        }
        currActiveMenu = Menu.HOME
        router.navigateTo(screens.home())
    }

    fun openContacts() {
        Toast.makeText(ProEventApp.instance, "CONTACTS", Toast.LENGTH_LONG).show()
    }

    fun openChat() {
        Toast.makeText(ProEventApp.instance, "CHAT", Toast.LENGTH_LONG).show()
    }

    fun openEvents() {
        Toast.makeText(ProEventApp.instance, "EVENTS", Toast.LENGTH_LONG).show()
    }

    fun openSettings() {
        if (currActiveMenu == Menu.SETTINGS) {
            return
        }
        currActiveMenu = Menu.SETTINGS
        router.navigateTo(screens.settings())
    }

    fun backClicked() {
        router.exit()
    }
}