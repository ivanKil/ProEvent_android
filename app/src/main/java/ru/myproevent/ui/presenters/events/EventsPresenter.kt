package ru.myproevent.ui.presenters.events

import com.github.terrakok.cicerone.Router
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.domain.models.repositories.events.IProEventEventsRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import ru.myproevent.ui.presenters.events.adapter.IEventsListPresenter
import javax.inject.Inject

class EventsPresenter(localRouter: Router) : BaseMvpPresenter<EventsView>(localRouter) {

    @Inject
    lateinit var eventsRepository: IProEventEventsRepository

    inner class EventsListPresenter(
        private val itemClickListener: ((Event) -> Unit)? = null,
        private val editIconClickListener: ((Event) -> Unit)? = null
    ) : IEventsListPresenter {

        private var events = listOf<Event>()

        override fun getCount() = events.size

        override fun bindView(view: IEventItemView) {
            val event = events[view.pos]
            view.setName(event.name)
            view.setTime(event.startDate.toString())
        }

        override fun onEditButtonClick(view: IEventItemView) {
            editIconClickListener?.invoke(events[view.pos])
        }

        override fun onItemClick(view: IEventItemView) {
            itemClickListener?.invoke(events[view.pos])
        }

        fun setData(data: List<Event>) {
            events = data
            viewState.updateList()
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

    fun loadData() {
        eventsRepository.getEvents()
            .observeOn(uiScheduler)
            .subscribe({ data ->
                eventsListPresenter.setData(data)
            }, {
                viewState.showToast("ПРОИЗОШЛА ОШИБКА: ${it.message}")
            }).disposeOnDestroy()
    }
}