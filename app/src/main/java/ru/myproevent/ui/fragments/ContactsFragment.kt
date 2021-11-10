package ru.myproevent.ui.fragments

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentContactsBinding
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.ui.adapters.contacts.ContactsRVAdapter
import ru.myproevent.ui.presenters.contacts.ContactsPresenter
import ru.myproevent.ui.presenters.contacts.ContactsView
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu


class ContactsFragment : BaseMvpFragment(), ContactsView {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private var _vb: FragmentContactsBinding? = null
    private val vb get() = _vb!!

    private var isFilterOptionsExpanded = false

    // TODO: копирует поле licenceTouchListener из RegistrationFragment
    private val filterOptionTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            }
            MotionEvent.ACTION_UP -> with(v as TextView) {
                setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
                setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
                performClick()
            }
        }
        true
    }

    override val presenter by moxyPresenter {
        ContactsPresenter().apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    var adapter: ContactsRVAdapter? = null

    private var confirmScreenCallBack: ((confirmed: Boolean) -> Unit)? = null

    private fun showFilterOptions() {
        isFilterOptionsExpanded = true
        with(vb) {
            filter.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.ProEvent_bright_orange_300
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
            searchEdit.hideKeyBoard() // TODO: нужно вынести это в вызов предществующий данному, чтобы тень при скрытии клавиатуры отображалась корректно
            searchInput.visibility = GONE
            shadow.visibility = VISIBLE
            allContacts.visibility = VISIBLE
            outgoingContacts.visibility = VISIBLE
            incomingContacts.visibility = VISIBLE
        }
    }

    private fun hideFilterOptions() {
        isFilterOptionsExpanded = false
        with(vb) {
            filter.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.ProEvent_blue_800
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
            searchInput.visibility = VISIBLE
            shadow.visibility = GONE
            allContacts.visibility = GONE
            outgoingContacts.visibility = GONE
            incomingContacts.visibility = GONE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainView).selectItem(Menu.CONTACTS)
        _vb = FragmentContactsBinding.inflate(inflater, container, false)
        return vb.apply {
            allContacts.setOnTouchListener(filterOptionTouchListener)
            outgoingContacts.setOnTouchListener(filterOptionTouchListener)
            incomingContacts.setOnTouchListener(filterOptionTouchListener)
            addContact.setOnClickListener { presenter.addContact() }
            addFirstContact.setOnClickListener { presenter.addContact() }
            filter.setOnClickListener {
                if (!isFilterOptionsExpanded) {
                    showFilterOptions()
                } else {
                    hideFilterOptions()
                }
            }
            filterHitArea.setOnClickListener { filter.performClick() }
            shadow.setOnClickListener { hideFilterOptions() }
            btnYes.setOnClickListener { confirmScreenCallBack?.invoke(true) }
            btnNo.setOnClickListener { confirmScreenCallBack?.invoke(false) }
        }.root.apply {
            // https://stackoverflow.com/questions/20103888/animatelayoutchanges-does-not-work-well-with-nested-layout
            vb.container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.init()
    }

    override fun init() = with(vb) {
        rvContacts.layoutManager = LinearLayoutManager(context)
        adapter = ContactsRVAdapter(presenter.contactsListPresenter)
        rvContacts.adapter = adapter
    }

    override fun hideConfirmationScreen() {
        vb.container.visibility = VISIBLE
        vb.confirmScreen.visibility = GONE
    }

    override fun showConfirmationScreen(
        action: Contact.Action,
        callBack: ((confirmed: Boolean) -> Unit)?
    ) {
        vb.tvConfirmMsg.text = when (action) {
            Contact.Action.ACCEPT ->
                getString(R.string.accept_contact_request_question)
            Contact.Action.CANCEL ->
                getString(R.string.cancel_request_question)
            Contact.Action.DECLINE ->
                getString(R.string.decline_contact_request_question)
            Contact.Action.DELETE ->
                getString(R.string.delete_contact_question)
            else -> null
        }

        confirmScreenCallBack = callBack

        vb.container.visibility = INVISIBLE
        vb.confirmScreen.visibility = VISIBLE
    }

    override fun updateList() {
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
            with(vb) {
                if (adapter!!.itemCount == 0) {
                    progressBar.visibility = GONE
                    scroll.visibility = VISIBLE
                    noContacts.visibility = VISIBLE
                    noContactsText.visibility = VISIBLE
                    addFirstContact.visibility = VISIBLE
                } else if (adapter!!.itemCount > 0) {
                    progressBar.visibility = GONE
                    noContacts.visibility = GONE
                    noContactsText.visibility = GONE
                    addFirstContact.visibility = GONE
                    scroll.visibility = VISIBLE
                    rvContacts.visibility = VISIBLE
                }
            }
        }
    }

    override fun showToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}