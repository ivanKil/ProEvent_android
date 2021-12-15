package ru.myproevent.ui.fragments.settings

import android.os.Bundle
import android.view.View
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentSettingsBinding
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab
import ru.myproevent.ui.presenters.settings.settings_list.SettingsPresenter
import ru.myproevent.ui.presenters.settings.settings_list.SettingsView

class SettingsFragment : BaseMvpFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate),
    SettingsView {

    override val presenter by moxyPresenter {
        SettingsPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        account.setOnClickListener { presenter.account() }
        security.setOnClickListener { presenter.security() }
        subscriptions.setOnClickListener { }
        help.setOnClickListener { }
        about.setOnClickListener { }
        logout.setOnClickListener { presenter.logout() }
    }

    override fun logout() {
        (requireActivity() as BottomNavigationView).resetState()
    }
}