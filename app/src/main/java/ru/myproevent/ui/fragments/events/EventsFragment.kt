package ru.myproevent.ui.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentEventsBinding
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.events.EventsPresenter
import ru.myproevent.ui.presenters.events.EventsView
import ru.myproevent.ui.presenters.events.adapter.EventsRVAdapter
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab


class EventsFragment : BaseMvpFragment(), EventsView {

    companion object {
        fun newInstance() = EventsFragment()
    }

    private var _vb: FragmentEventsBinding? = null
    private val vb get() = _vb!!

    override val presenter by moxyPresenter {
        EventsPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    var adapter: EventsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as BottomNavigationView).checkTab(Tab.EVENTS)
        _vb = FragmentEventsBinding.inflate(inflater, container, false)

        initFilter()
        return vb.root
    }

    private fun initFilter() = with(vb) {
        initFilterButton()

        allEvents.setOnTouchListener(filterOptionOnTouchListener)
        actualEvents.setOnTouchListener(filterOptionOnTouchListener)
        completedEvents.setOnTouchListener(filterOptionOnTouchListener)

        allEvents.setOnClickListener { presenter.onFilterChosen(Event.Status.ALL) }
        actualEvents.setOnClickListener { presenter.onFilterChosen(Event.Status.ACTUAL) }
        completedEvents.setOnClickListener { presenter.onFilterChosen(Event.Status.COMPLETED) }
    }

    private fun initFilterButton() = with(vb) {

        filterHitArea.setOnClickListener {
            searchEdit.hideKeyBoard()
            if (filter.visibility == GONE) {
                filterBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.ProEvent_bright_orange_300
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                showFilterOptions()
            } else {
                filterBtn.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.ProEvent_blue_800
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                hideFilterOptions()
            }
        }
    }

    override fun showFilterOptions() = with(vb) {
        shadow.visibility = VISIBLE
        filter.visibility = VISIBLE

    }

    override fun hideFilterOptions() = with(vb) {
        shadow.visibility = GONE
        filter.visibility = GONE
    }

    override fun init() = with(vb) {
        rvEvents.layoutManager = LinearLayoutManager(context)
        adapter = EventsRVAdapter(presenter.eventsListPresenter)
        rvEvents.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    private val filterOptionOnTouchListener = View.OnTouchListener { v, event ->
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

    override fun selectFilterOption(option: Event.Status) = with(vb) {
        allEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
        allEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
        actualEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
        actualEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))
        completedEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
        completedEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_800))

        when (option) {
            Event.Status.ALL -> {
                title.textSize = 20F
                title.text = getString(R.string.events_title)
                allEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                allEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            }
            Event.Status.ACTUAL -> {
                title.text = getString(R.string.actual_events)
                title.textSize = 14F
                actualEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                actualEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            }
            Event.Status.COMPLETED -> {
                title.text = getString(R.string.completed_events)
                title.textSize = 14F
                completedEvents.setBackgroundColor(ProEventApp.instance.getColor(R.color.ProEvent_blue_600))
                completedEvents.setTextColor(ProEventApp.instance.getColor(R.color.ProEvent_white))
            }
            else -> {
                title.text = getString(R.string.empty_string)
            }
        }
    }

    override fun setNoEventsLayoutVisibility(visible: Boolean) {
        vb.noEventsLayout.visibility = if (visible) VISIBLE
        else GONE
    }

    override fun showToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}