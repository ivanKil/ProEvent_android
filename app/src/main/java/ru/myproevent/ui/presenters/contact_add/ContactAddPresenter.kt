package ru.myproevent.ui.presenters.contact_add

import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.domain.model.repositories.contacts.IProEventContactsRepository
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class ContactAddPresenter : BaseMvpPresenter<ContactAddView>() {
    private inner class ContactAddObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            // TODO: это должен быть локальный router
            router.newRootScreen(screens.contacts())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver(error.message))
                .disposeOnDestroy()
        }
    }

    @Inject
    lateinit var contactsRepository: IProEventContactsRepository

    @Inject
    lateinit var profilesRepository: IProEventProfilesRepository

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    private var isSearchMode = true

    // TODO: заменить id на email когда на сервере появится функция поиска профиля по email
    fun addContact(id: Long) {
        contactsRepository
            .addContact(id)
            .observeOn(uiScheduler)
            .subscribeWith(ContactAddObserver())
            .disposeOnDestroy()
    }

    override fun backPressed(): Boolean {
        return if (!isSearchMode) {
            viewState.showSearchForm()
            false
        } else {
            super.backPressed()
        }
    }
}