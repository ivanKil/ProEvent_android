package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpView
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentContactBinding
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.ui.presenters.BaseMvpPresenter

class ContactFragment : BaseMvpFragment(), MvpView {

    private lateinit var contact : Contact

    companion object {
        private const val BUNDLE_CONTACT = "contact"
        fun newInstance(contact: Contact) = ContactFragment().apply {
            arguments = Bundle().apply { putParcelable(BUNDLE_CONTACT, contact) }
        }
    }

    private var _vb: FragmentContactBinding? = null
    private val vb get() = _vb!!

    override val presenter by moxyPresenter {
        BaseMvpPresenter<MvpView>().apply { ProEventApp.instance.appComponent.inject(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactBinding.inflate(inflater, container, false).also { _vb = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Contact>(BUNDLE_CONTACT)?.let { contact = it }
        fillFields()
    }

    fun fillFields(){
        contact.fullName?.let { vb.nameEdit.setText(it)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}