package ru.myproevent.ui.fragments

import androidx.viewbinding.ViewBinding
import moxy.MvpAppCompatFragment
import ru.myproevent.databinding.FragmentAuthorizationBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.BaseMvpPresenter

abstract class BaseMvpFragment : MvpAppCompatFragment(), BackButtonListener {
    protected abstract val presenter: BaseMvpPresenter<*>
    override fun backPressed() = presenter.backPressed()
}
