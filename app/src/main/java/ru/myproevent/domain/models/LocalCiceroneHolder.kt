package ru.myproevent.domain.models

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.myproevent.ui.presenters.main.Tab
import java.util.HashMap

class LocalCiceroneHolder {
    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> =
        containers.getOrPut(containerTag) {
            Cicerone.create()
        }

    fun clear(){
        containers.clear()
    }
}