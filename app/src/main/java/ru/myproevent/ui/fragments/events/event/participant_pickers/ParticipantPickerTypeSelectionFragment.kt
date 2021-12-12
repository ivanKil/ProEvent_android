package ru.myproevent.ui.fragments.events.event.participant_pickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentParticipantPickerTypeSelectionBinding
import ru.myproevent.ui.BackButtonListener
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.events.event.participant_pickers.ParticipantPickerTypeSelectionPresenter
import ru.myproevent.ui.presenters.events.event.participant_pickers.ParticipantPickerTypeSelectionView
import ru.myproevent.ui.presenters.main.RouterProvider

class ParticipantPickerTypeSelectionFragment : BaseMvpFragment(),
    ParticipantPickerTypeSelectionView,
    BackButtonListener {
    private var _view: FragmentParticipantPickerTypeSelectionBinding? = null
    private val view get() = _view!!

    override val presenter by moxyPresenter {
        ParticipantPickerTypeSelectionPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        fun newInstance() = ParticipantPickerTypeSelectionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _view = FragmentParticipantPickerTypeSelectionBinding.inflate(inflater, container, false)
        return view.apply {
            back.setOnClickListener { presenter.onBackPressed() }
            backHitArea.setOnClickListener { back.performClick() }
            pickFromContacts.setOnClickListener { presenter.pickFromContacts() }
            pickByEmail.setOnClickListener { presenter.pickByEmail() }
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _view = null
    }

    override fun showMessage(message: String) {
        Toast.makeText(ProEventApp.instance, message, Toast.LENGTH_LONG).show()
    }
}