package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentSettingsBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu
import ru.myproevent.ui.presenters.settings.SettingsPresenter
import ru.myproevent.ui.presenters.settings.SettingsView

class SettingsFragment : BaseMvpFragment(), SettingsView, BackButtonListener {
    private var _view: FragmentSettingsBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        SettingsPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).selectItem(Menu.SETTINGS)
        _view = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            account.setOnClickListener { presenter.account() }
            security.setOnClickListener { presenter.security() }
            subscriptions.setOnClickListener { }
            help.setOnClickListener { }
            about.setOnClickListener { }
            logout.setOnClickListener { presenter.logout() }
        }
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}