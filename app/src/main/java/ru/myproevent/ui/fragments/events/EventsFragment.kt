package ru.myproevent.ui.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentEventsBinding
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
        return vb.root
    }

    override fun init() = with(vb) {
        rvEvents.layoutManager = LinearLayoutManager(context)
        adapter = EventsRVAdapter(presenter.eventsListPresenter)
        rvEvents.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}