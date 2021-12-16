package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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

    override fun showMessage(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
