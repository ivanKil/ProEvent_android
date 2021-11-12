package ru.myproevent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentHomeBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.home.HomePresenter
import ru.myproevent.ui.presenters.home.HomeView
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu

class HomeFragment : BaseMvpFragment(), HomeView, BackButtonListener {
    private var _view: FragmentHomeBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        HomePresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentHomeBinding.inflate(inflater, container, false).apply {
            id.text = "ID: ${presenter.getId()}"
            token.text = "token:\n${presenter.getToken()}"
            Log.d("[MYLOG]", "token: ${presenter.getToken()}")
        }
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}