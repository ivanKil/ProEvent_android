package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import moxy.MvpAppCompatFragment
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.presenters.BaseMvpPresenter
import ru.myproevent.ui.presenters.BaseMvpView

abstract class BaseMvpFragment<Binding : ViewBinding>(
    private val bindingFactory: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> Binding
) : MvpAppCompatFragment(), BaseMvpView, BackButtonListener {

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    protected abstract val presenter: BaseMvpPresenter<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = bindingFactory(inflater, container, false).also { _binding = it }.root

    override fun showMessage(message: String) {
        // TODO: отрефакторить
        // https://github.com/terrakok/Cicerone/issues/106
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val prev: Fragment? = parentFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val newFragment: DialogFragment =
            ProEventMessageDialog.newInstance(message)
        newFragment.show(ft, "dialog")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
