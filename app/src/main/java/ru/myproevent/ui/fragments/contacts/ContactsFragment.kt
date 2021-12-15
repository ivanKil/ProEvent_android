package ru.myproevent.ui.fragments.contacts

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentContactsBinding
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.domain.models.entities.Contact.Status
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.contacts.contacts_list.ContactsPresenter
import ru.myproevent.ui.presenters.contacts.contacts_list.ContactsView
import ru.myproevent.ui.presenters.contacts.contacts_list.adapters.ContactsRVAdapter
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab

class ContactsFragment : BaseMvpFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate),
    ContactsView {

    companion object {
        fun newInstance() = ContactsFragment()
    }

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

    private fun selectFilterOption(option: TextView) {
        with(binding) {
            allContacts.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            allContacts.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
            outgoingContacts.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            outgoingContacts.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
            incomingContacts.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            incomingContacts.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))

            option.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
            option.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))

            when (option.id) {
                R.id.all_contacts -> {
                    // TODO: Вынести в ресурсы
                    title.text = "Контакты: Все"
                    noContactsText.text = "У вас пока нет контактов"
                }
                R.id.outgoing_contacts -> {
                    title.text = "Контакты: Исходящие"
                    noContactsText.text = "У вас нет активных запросов"
                }

                R.id.incoming_contacts -> {
                    title.text = "Контакты: Входящие"
                    noContactsText.text = "У вас нет активных запросов"
                }
            }
        }
    }

    override val presenter by moxyPresenter {
        ContactsPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    var adapter: ContactsRVAdapter? = null

    private var confirmScreenCallBack: ((confirmed: Boolean) -> Unit)? = null

    private fun showFilterOptions() {
        isFilterOptionsExpanded = true
        with(binding) {
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
        with(binding) {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        allContacts.setOnTouchListener(filterOptionTouchListener)
        allContacts.setOnClickListener {
            selectFilterOption(allContacts)
            presenter.loadData(Status.ALL)
            hideFilterOptions()
        }
        outgoingContacts.setOnTouchListener(filterOptionTouchListener)
        outgoingContacts.setOnClickListener {
            selectFilterOption(outgoingContacts)
            presenter.loadData(Status.PENDING)
            hideFilterOptions()
        }
        incomingContacts.setOnTouchListener(filterOptionTouchListener)
        incomingContacts.setOnClickListener {
            selectFilterOption(incomingContacts)
            presenter.loadData(Status.REQUESTED)
            hideFilterOptions()
        }
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

        // https://stackoverflow.com/questions/20103888/animatelayoutchanges-does-not-work-well-with-nested-layout
        container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    override fun onResume() {
        super.onResume()
        presenter.init()
    }

    override fun init() = with(binding) {
        rvContacts.layoutManager = LinearLayoutManager(context)
        adapter = ContactsRVAdapter(presenter.contactsListPresenter)
        rvContacts.adapter = adapter
    }

    override fun hideConfirmationScreen() {
        binding.container.visibility = VISIBLE
        binding.confirmScreen.visibility = GONE
    }

    override fun showConfirmationScreen(
        action: Contact.Action,
        callBack: ((confirmed: Boolean) -> Unit)?
    ) {
        binding.tvConfirmMsg.text = when (action) {
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

        binding.container.visibility = INVISIBLE
        binding.confirmScreen.visibility = VISIBLE
    }

    override fun updateContactsList() {
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
            with(binding) {
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
}