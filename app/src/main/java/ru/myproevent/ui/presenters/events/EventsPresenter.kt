package ru.myproevent.ui.presenters.events

import com.github.terrakok.cicerone.Router
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.domain.models.repositories.events.IProEventEventsRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import ru.myproevent.ui.presenters.events.adapter.IEventsListPresenter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventsPresenter(localRouter: Router) : BaseMvpPresenter<EventsView>(localRouter) {

    @Inject
    lateinit var eventsRepository: IProEventEventsRepository

    inner class EventsListPresenter(
        private val itemClickListener: ((Event) -> Unit)? = null,
        private val editIconClickListener: ((Event) -> Unit)? = null
    ) : IEventsListPresenter {

        private var allEvents = listOf<Event>()
        private var events = listOf<Event>()
        private var eventFilter = Event.Status.ALL

        override fun getCount() = events.size

        override fun bindView(view: IEventItemView) {
            val event = events[view.pos]
            view.setName(event.name)
            view.setTime(formatDate(event.startDate, event.endDate))
        }

        override fun onEditButtonClick(view: IEventItemView) {
            editIconClickListener?.invoke(events[view.pos])
        }

        override fun onItemClick(view: IEventItemView) {
            itemClickListener?.invoke(events[view.pos])
        }

        fun setData(data: List<Event>) {
            allEvents = data
            filter(eventFilter)
        }

        fun filter(status: Event.Status) {
            eventFilter = status
            events = if (status == Event.Status.ALL) allEvents
            else allEvents.filter { it.status == status }
            viewState.updateList()
        }

        fun formatDate(start: Date, end: Date): String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val sb = StringBuilder(dateFormat.format(start))
            sb.append(" - ")
            sb.append(dateFormat.format(end))
            return sb.toString()
        }
    }


    val eventsListPresenter = EventsListPresenter({
        //localRouter.navigateTo(screens.event(it))
    }, {
        //localRouter.navigateTo(screens.event(it))
    })

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()
    }

    private fun loadData() {
        eventsRepository.getEvents()
            .observeOn(uiScheduler)
            .subscribe({ data ->
                viewState.setNoEventsLayoutVisibility(data.isEmpty())
                eventsListPresenter.setData(data)
            }, {
                viewState.showToast("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }

    fun onFilterChosen(status: Event.Status) {
        viewState.hideFilterOptions()
        viewState.selectFilterOption(status)

        eventsListPresenter.filter(status)
    }

}