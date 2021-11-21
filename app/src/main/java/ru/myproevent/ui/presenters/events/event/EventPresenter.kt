package ru.myproevent.ui.presenters.events.event

import android.widget.Toast
import com.github.terrakok.cicerone.Router
import ru.myproevent.ProEventApp
import ru.myproevent.ui.presenters.BaseMvpPresenter
import ru.myproevent.ui.presenters.home.HomeView

class EventPresenter(localRouter: Router) : BaseMvpPresenter<HomeView>(localRouter){
    fun finishEvent(){
        Toast.makeText(ProEventApp.instance.applicationContext, "finishEvent", Toast.LENGTH_SHORT).show()
    }

    fun copyEvent(){
        Toast.makeText(ProEventApp.instance.applicationContext, "copyEvent", Toast.LENGTH_SHORT).show()
    }
}