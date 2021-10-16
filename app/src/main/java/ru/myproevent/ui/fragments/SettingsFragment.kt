package ru.myproevent.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentSettingsBinding
import ru.myproevent.domain.di.ProEventScreensComponent
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu
import ru.myproevent.ui.presenters.presenter.SettingsPresenter
import ru.myproevent.ui.presenters.presenter.SettingsView
import javax.inject.Inject

class SettingsFragment : MvpAppCompatFragment(), SettingsView, BackButtonListener {
    private var _view: FragmentSettingsBinding? = null
    private val view get() = _view!!

    @Inject
    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    @ProvidePresenter
    fun provide() = presenter

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private var proEventScreensComponent: ProEventScreensComponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        proEventScreensComponent =
            (requireActivity().application as? ProEventApp)
                ?.proEventApplicationComponent
                ?.proEventScreensComponent()
                ?.build()
                ?.also { it.inject(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).selectItem(Menu.SETTINGS)
        _view = FragmentSettingsBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}