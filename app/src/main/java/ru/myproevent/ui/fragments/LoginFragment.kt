package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentLoginBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.login.LoginPresenter
import ru.myproevent.ui.presenters.login.LoginView

class LoginFragment : MvpAppCompatFragment(), LoginView, BackButtonListener {
    private var _view: FragmentLoginBinding? = null
    private val view get() = _view!!

    private val presenter by moxyPresenter {
        LoginPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentLoginBinding.inflate(inflater, container, false).apply {
            confirmLogin.setOnClickListener { presenter.confirmLogin() }
        }
        return view.root
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}