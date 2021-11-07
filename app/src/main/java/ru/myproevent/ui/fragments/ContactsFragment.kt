package ru.myproevent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentContactsBinding
import ru.myproevent.ui.adapters.contacts.ContactsRVAdapter
import ru.myproevent.ui.presenters.contacts.ContactsPresenter
import ru.myproevent.ui.presenters.contacts.ContactsView

class ContactsFragment : BaseMvpFragment(), ContactsView {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private var _vb: FragmentContactsBinding? = null
    private val vb get() = _vb!!

    override val presenter by moxyPresenter {
        ContactsPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    var adapter: ContactsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = FragmentContactsBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun init() = with(vb) {
        rvContacts.layoutManager = LinearLayoutManager(context)
        adapter = ContactsRVAdapter(presenter.contactsListPresenter)
        rvContacts.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}