package ru.myproevent.ui.fragments.events.event.participant_pickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.R
import ru.myproevent.databinding.FragmentContactAddBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.events.event.participant_pickers.participant_by_email_picker.ParticipantByEmailPickerPresenter
import ru.myproevent.ui.presenters.events.event.participant_pickers.participant_by_email_picker.ParticipantByEmailPickerView
import ru.myproevent.ui.presenters.main.BottomNavigationView
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab

class ParticipantByEmailPickerFragment : BaseMvpFragment(), ParticipantByEmailPickerView, BackButtonListener {
    private var _view: FragmentContactAddBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        ParticipantByEmailPickerPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = ParticipantByEmailPickerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        with(requireActivity() as BottomNavigationView){
            showBottomNavigation()
            checkTab(Tab.HOME)
        }
        _view = FragmentContactAddBinding.inflate(inflater, container, false).apply {
            contactAddExplanation.text = getString(R.string.participant_add_by_email_explanation)
            titleButton.setOnClickListener { presenter.onBackPressed() }
        }
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }
}