package ru.myproevent.ui.presenters.contacts

import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.Page
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.domain.model.entities.Status
import ru.myproevent.domain.model.repositories.contacts.IProEventContactsRepository
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.adapters.contacts.IContactItemView
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

    inner class ContactsListPresenter(private var itemClickListener: ((IContactItemView, Contact) -> Unit)? = null) :
        IContactsListPresenter {

        private val contactDTOs = mutableListOf<ContactDto>()

        private var size = 0

        private val contacts = mutableListOf<Contact>()

        override fun getCount() = size

        override fun bindView(view: IContactItemView) {
            val pos = view.pos

            if (pos < contacts.size) {
                contacts[pos].apply {
                    fullName?.let { view.setName(it) }
                    description?.let { view.setDescription(it) }
                    imgUri?.let { view.loadImg(it) }
                }
            } else {
                profilesRepository.getContact(contactDTOs[pos])
                    .observeOn(uiScheduler)
                    .subscribe({
                        contacts.add(it)
                        bindView(view)
                    }, {
                        println("Error: ${it.message}")
                    }).disposeOnDestroy()
            }
        }

        override fun onItemClick(itemView: IContactItemView) {
            itemClickListener?.invoke(itemView, contacts[itemView.pos])
        }

        fun setData(data: List<ContactDto>, size: Int) {
            contactDTOs.clear()
            contacts.clear()
            contactDTOs.addAll(data)
            this.size = size

            viewState.updateList()
        }
    }

    val contactsListPresenter = ContactsListPresenter { itemview, contact ->
        router.navigateTo(screens.contact(contact))
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()
    }

    private fun loadData() {
        contactsRepository.getContacts(1, Int.MAX_VALUE, Status.ALL)
            .observeOn(uiScheduler)
            .subscribe({ data ->
                contactsListPresenter.setData(data.content, data.totalElements.toInt())
            }, {
                println("Error: ${it.message}")
            }).disposeOnDestroy()
    }

}