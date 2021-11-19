package ru.myproevent.ui.fragments.events

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import moxy.MvpAppCompatFragment
import ru.myproevent.databinding.FragmentEventsBinding
import ru.myproevent.ui.presenters.events.EventsView
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.Tab


class EventsFragment : MvpAppCompatFragment(), EventsView {

    companion object {
        fun newInstance() = EventsFragment()
    }

    private var _vb: FragmentEventsBinding? = null
    private val vb get() = _vb!!


//    override val presenter by moxyPresenter {
//        ContactsPresenter((parentFragment as RouterProvider).router).apply {
//            ProEventApp.instance.appComponent.inject(this)
//        }
//    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as BottomNavigationView).checkTab(Tab.EVENTS)
        _vb = FragmentEventsBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun init() = with(vb) {
    }

    override fun showToast(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun updateList() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}