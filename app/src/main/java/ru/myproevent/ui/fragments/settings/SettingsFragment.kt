package ru.myproevent.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentSettingsBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.Tab
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.settings.settings_list.SettingsPresenter
import ru.myproevent.ui.presenters.settings.settings_list.SettingsView

class SettingsFragment : BaseMvpFragment(), SettingsView, BackButtonListener {
    private var _view: FragmentSettingsBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        SettingsPresenter((parentFragment as RouterProvider).router).apply {
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
        (requireActivity() as BottomNavigationView).checkTab(Tab.SETTINGS)
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