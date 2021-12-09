package ru.myproevent.ui.fragments.authorization

import android.os.Bundle
import android.view.View
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentRecoveryBinding
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.authorization.recovery.RecoveryPresenter
import ru.myproevent.ui.presenters.authorization.recovery.RecoveryView
import ru.myproevent.ui.presenters.main.RouterProvider

class RecoveryFragment : BaseMvpFragment<FragmentRecoveryBinding>(FragmentRecoveryBinding::inflate),
    RecoveryView {

    override val presenter by moxyPresenter {
        RecoveryPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = RecoveryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authorizeHitArea.setOnClickListener { presenter.authorize() }
    }
}