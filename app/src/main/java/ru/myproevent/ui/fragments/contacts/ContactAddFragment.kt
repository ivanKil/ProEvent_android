package ru.myproevent.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentContactAddBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.contacts.contact_add.ContactAddPresenter
import ru.myproevent.ui.presenters.contacts.contact_add.ContactAddView
import ru.myproevent.ui.presenters.main.RouterProvider

class ContactAddFragment : BaseMvpFragment(), ContactAddView, BackButtonListener {
    private var _view: FragmentContactAddBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        ContactAddPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = ContactAddFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = FragmentContactAddBinding.inflate(inflater, container, false)
        return view.apply {
            searchContact.setOnClickListener {
                try {
                    presenter.addContact(emailEdit.text.toString().toLong())
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Значение должно быть числом обозначающее id пользователя", Toast.LENGTH_LONG).show()
                }
            }
            titleButton.setOnClickListener { presenter.onBackPressed() }
        }.root
    }

    override fun showInvitationForm() = with(view) {
        contactAddExplanation.visibility = GONE
        searchContact.visibility = GONE
        contactInviteExplanation.visibility = VISIBLE
        nameInputContainer.visibility = VISIBLE
        inviteContact.visibility = VISIBLE
    }

    override fun showSearchForm() = with(view) {
        contactAddExplanation.visibility = VISIBLE
        searchContact.visibility = VISIBLE
        contactInviteExplanation.visibility = GONE
        nameInputContainer.visibility = GONE
        inviteContact.visibility = GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}