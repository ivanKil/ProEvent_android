package ru.myproevent.ui.fragments.authorization

import android.os.Bundle
import android.view.View
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentLoginBinding
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.authorization.login.LoginPresenter
import ru.myproevent.ui.presenters.authorization.login.LoginView
import ru.myproevent.ui.presenters.main.BottomNavigation
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab

class LoginFragment : BaseMvpFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    LoginView {

    override val presenter by moxyPresenter {
        LoginPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        confirmLogin.setOnClickListener { presenter.confirmLogin(loginEdit.text.toString()) }
    }

    override fun finishAuthorization() {
        (requireActivity() as BottomNavigation).openTab(Tab.HOME)
    }
}