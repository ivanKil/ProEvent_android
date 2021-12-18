package ru.myproevent.ui.presenters.events.event

import android.util.Log
import com.github.terrakok.cicerone.Router
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Address
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.domain.models.repositories.events.IProEventEventsRepository
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.domain.models.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import java.util.*
import javax.inject.Inject

class EventPresenter(localRouter: Router) : BaseMvpPresenter<EventView>(localRouter) {
    @Inject
    lateinit var eventsRepository: IProEventEventsRepository

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    @Inject
    lateinit var profilesRepository: IProEventProfilesRepository

    private var isParticipantsProfilesInitialized = false

    fun addEvent(
        name: String,
        startDate: Date,
        endDate: Date,
        address: Address?,
        description: String,
        participantsIds: LongArray,
        callback: ((Event?) -> Unit)? = null
    ) {
        eventsRepository
            .saveEvent(
                Event(
                    id = null,
                    name = name,
                    ownerUserId = loginRepository.getLocalId()!!,
                    eventStatus = Event.Status.ACTUAL,
                    startDate = startDate,
                    endDate = endDate,
                    description = description,
                    participantsUserIds = participantsIds,
                    city = null,
                    address = address,
                    mapsFileIds = null,
                    pointsPointIds = null,
                    imageFile = null
                )
            )
            .observeOn(uiScheduler)
            .subscribe({
                callback?.invoke(it)
                viewState.showMessage("Мероприятие создано")
            }, {
                callback?.invoke(null)
                viewState.showMessage("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }

    fun editEvent(event: Event, callback: ((Event?) -> Unit)? = null) {
        eventsRepository
            .editEvent(event)
            .observeOn(uiScheduler)
            .subscribe({
                callback?.invoke(event)
                viewState.showMessage("Изменения сохранены")
            }, {
                callback?.invoke(null)
                viewState.showMessage("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }

    fun finishEvent(event: Event) =
        localRouter.navigateTo(screens.eventActionConfirmation(event, Event.Status.COMPLETED))


    fun cancelEvent(event: Event) =
        localRouter.navigateTo(screens.eventActionConfirmation(event, Event.Status.CANCELLED))

    fun deleteEvent(event: Event) =
        localRouter.navigateTo(screens.eventActionConfirmation(event, null))

    fun copyEvent(event: Event) {
        eventsRepository
            .saveEvent(event)
            .observeOn(uiScheduler)
            .subscribe({
                localRouter.navigateTo(screens.event(it))
            }, {
                viewState.showMessage("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }

    fun pickParticipants() {
        localRouter.navigateTo(screens.participantPickerTypeSelection())
    }

    fun initParticipantsProfiles(participantsIds: LongArray) {
        if (isParticipantsProfilesInitialized) {
            return
        }
        isParticipantsProfilesInitialized = true
        for (id in participantsIds) {
            profilesRepository.getProfile(id)
                .observeOn(uiScheduler)
                .subscribe({ profileDto ->
                    viewState.addParticipantItemView(profileDto!!)
                }, {
                    Log.d("[FUCK]", "error: $it")
                    val profileDto = ProfileDto(
                        userId = id,
                        fullName = "Заглушка",
                        description = "Профиля нет, или не загрузился",
                    )
                    viewState.addParticipantItemView(profileDto)
                }).disposeOnDestroy()
        }
    }

    fun loadParticipantsProfiles(participants: Array<ProfileDto>) {
        for (participant in participants) {
            viewState.addParticipantItemView(participant)
        }
    }

    fun clearParticipants() {
        viewState.clearParticipants()
        isParticipantsProfilesInitialized = false
    }

    fun addEventPlace(address: Address?) {
        localRouter.navigateTo(screens.addEventPlace(address))
    }

    fun enableDescriptionEdit() {
        viewState.enableDescriptionEdit()
    }

    fun expandDescription() {
        viewState.expandDescription()
    }

    fun expandMaps() {
        viewState.expandMaps()
    }

    fun expandPoints() {
        viewState.expandPoints()
    }

    fun expandParticipants() {
        viewState.expandParticipants()
    }

    fun cancelEdit() {
        viewState.cancelEdit()
    }

    fun hideEditOptions() {
        viewState.hideEditOptions()
    }

    fun lockEdit() {
        viewState.lockEdit()
    }

    fun showMessage(message: String) {
        viewState.showMessage(message)
    }

    fun showAbsoluteBar(
        title: String,
        iconResource: Int?,
        iconTintResource: Int?,
        onCollapseScroll: Int,
        onCollapse: () -> Unit,
        onEdit: () -> Unit
    ) {
        viewState.showAbsoluteBar(
            title,
            iconResource,
            iconTintResource,
            onCollapseScroll,
            onCollapse,
            onEdit
        )
    }

    fun hideAbsoluteBar() {
        viewState.hideAbsoluteBar()
    }

    fun unlockNameEdit() {
        viewState.unlockNameEdit()
    }

    fun unlockDateEdit() {
        viewState.unlockDateEdit()
    }

    fun unlockLocationEdit() {
        viewState.unlockLocationEdit()
    }
}