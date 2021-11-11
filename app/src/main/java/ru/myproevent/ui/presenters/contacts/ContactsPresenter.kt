package ru.myproevent.ui.presenters.contacts

import android.util.Log
import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.domain.model.entities.Status
import ru.myproevent.domain.model.repositories.contacts.IProEventContactsRepository
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.adapters.contacts.IContactsListPresenter
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class ContactsPresenter : BaseMvpPresenter<ContactsView>() {

    @Inject
    lateinit var contactsRepository: IProEventContactsRepository

    @Inject
    lateinit var profilesRepository: IProEventProfilesRepository

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    inner class ContactsListPresenter(
        private var itemClickListener: ((IContactItemView, Contact) -> Unit)? = null,
        private var statusClickListener: ((Contact) -> Unit)? = null
    ) : IContactsListPresenter {

        private val contactDTOs = mutableListOf<ContactDto>()

        private var size = 0

        private var contacts = mutableListOf<Contact?>()

        override fun getCount() = size

        override fun bindView(view: IContactItemView) {
            val pos = view.pos

            if (contacts[pos] != null) {
                fillItemView(view, contacts[pos]!!)
            } else {
                val contactDto = contactDTOs[pos]
                profilesRepository.getContact(contactDto)
                    .observeOn(uiScheduler)
                    .subscribe({ contact ->
                        Log.d("[CONTACTS]", "contacts.add($contact)")
                        contacts[pos] = contact
                        fillItemView(view, contact)
                    }, {
                        println("Error: ${it.message}")
                        contacts[pos] = Contact(
                            contactDto.id,
                            fullName = "Заглушка",
                            description = "Профиля нет, или не загрузился",
                            status = Status.fromString(contactDto.status)
                        )
                        fillItemView(view, contacts[pos]!!)
                    }).disposeOnDestroy()
            }
        }

        private fun fillItemView(view: IContactItemView, contact: Contact) {
            contact.apply {
                if (!fullName.isNullOrEmpty()) {
                    view.setName(fullName!!)
                } else if (!nickName.isNullOrEmpty()) {
                    view.setName(nickName!!)
                } else {
                    view.setName(userId.toString())
                }
                if (!description.isNullOrEmpty()) {
                    view.setDescription(description!!)
                } else if (!nickName.isNullOrEmpty()) {
                    view.setDescription(nickName!!)
                } else {
                    view.setDescription("id пользователя: $userId")
                }
                //imgUri?.let { view.loadImg(it) }
                status?.let { view.setStatus(it) }
            }
        }

        override fun onItemClick(view: IContactItemView) {
            contacts[view.pos]?.let { itemClickListener?.invoke(view, it) }
        }

        override fun onStatusClick(view: IContactItemView) {
            contacts[view.pos]?.let { statusClickListener?.invoke(it) }
        }

        fun setData(data: List<ContactDto>, size: Int) {
            this.size = 0
            contactDTOs.clear()
            contactDTOs.addAll(data)
            contacts = MutableList(size) { null }
            this.size = size
            viewState.updateList()
        }
    }

    val contactsListPresenter = ContactsListPresenter({ _, contact ->
        router.navigateTo(screens.contact(contact))
    }, { contact ->
        val action = when (contact.status) {
            Status.DECLINED -> Contact.Action.DELETE
            Status.PENDING -> Contact.Action.CANCEL
            Status.REQUESTED -> Contact.Action.ACCEPT
            else -> return@ContactsListPresenter
        }

        viewState.showConfirmationScreen(action) { confirmed ->
            viewState.hideConfirmationScreen()
            if (!confirmed) return@showConfirmationScreen
            performActionOnContact(contact, action)
        }
    })

    private fun performActionOnContact(contact: Contact, action: Contact.Action) {
        when (action) {
            Contact.Action.ACCEPT -> contactsRepository.acceptContact(contact.userId)
            Contact.Action.CANCEL, Contact.Action.DELETE -> contactsRepository.deleteContact(contact.userId)
            Contact.Action.DECLINE -> contactsRepository.declineContact(contact.userId)
            else -> return
        }.observeOn(uiScheduler)
            .subscribe({ loadData() }, { viewState.showToast("Не удалось выполнить действие") })
            .disposeOnDestroy()
    }

    fun init() {
        viewState.init()
        loadData()
    }

    fun addContact() {
        router.navigateTo(screens.contactAdd())
    }

    fun loadData(status: Status = Status.ALL) {
        contactsRepository.getContacts(1, Int.MAX_VALUE, status)
            .observeOn(uiScheduler)
            .subscribe({ data ->
                contactsListPresenter.setData(data.content, data.totalElements.toInt())
            }, {
                viewState.showToast("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }

}