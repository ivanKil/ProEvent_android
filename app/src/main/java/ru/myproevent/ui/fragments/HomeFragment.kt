package ru.myproevent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentHomeBinding
import ru.myproevent.ui.presenters.home.HomePresenter
import ru.myproevent.ui.presenters.home.HomeView
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab

class HomeFragment : BaseMvpFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), HomeView {

    override val presenter by moxyPresenter {
        HomePresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        with(requireActivity() as BottomNavigationView) {
            showBottomNavigation()
            checkTab(Tab.HOME)
        }

        Log.d("[MYLOG]", "token: ${presenter.getToken()}")
        id.text = "ID: ${presenter.getId()}"
        token.text = "token:\n${presenter.getToken()}"
    }
}