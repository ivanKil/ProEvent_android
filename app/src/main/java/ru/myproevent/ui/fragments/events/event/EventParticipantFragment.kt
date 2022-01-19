package ru.myproevent.ui.fragments.events.event

import android.os.Bundle
import android.view.View
import android.widget.Toast
import moxy.ktx.moxyPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.databinding.FragmentEventParticipantBinding
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.ui.fragments.BaseMvpFragment
import ru.myproevent.ui.presenters.events.event.participant.EventParticipantPresenter
import ru.myproevent.ui.presenters.events.event.participant.EventParticipantView
import ru.myproevent.ui.presenters.main.BottomNavigation
import ru.myproevent.ui.presenters.main.RouterProvider
import ru.myproevent.ui.presenters.main.Tab

class EventParticipantFragment :
    BaseMvpFragment<FragmentEventParticipantBinding>(FragmentEventParticipantBinding::inflate),
    EventParticipantView {

    private val profileDto: ProfileDto by lazy {
        requireArguments().getParcelable(PROFILE_ARG)!!
    }

    override val presenter by moxyPresenter {
        EventParticipantPresenter((parentFragment as RouterProvider).router).apply {
            ProEventApp.instance.appComponent.inject(this)
        }
    }

    companion object {
        val PROFILE_ARG = "PROFILE"
        fun newInstance(profileDto: ProfileDto) = EventParticipantFragment().apply {
            arguments = Bundle().apply { putParcelable(PROFILE_ARG, profileDto) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        with(profileDto){
            title.text = fullName ?: nickName ?: "[#$userId]"
            pointEdit.setText("[ДАННОЕ ПОЛЕ ПОКА НЕ РЕАЛИЗОВАНО НА СЕРВЕРЕ]")
            positionEdit.setText(position)
            toChat.setOnClickListener { presenter.openChat(profileDto.userId) }
            toProfile.setOnClickListener { presenter.openProfile(profileDto) }
            removeParticipant.setOnClickListener { presenter.removeParticipant(profileDto.userId) }
        }
    }

    override fun openChat(userId: Long) {
        (requireActivity() as BottomNavigation).openTab(Tab.CHAT)
        Toast.makeText(
            ProEventApp.instance.applicationContext,
            "[ЗАГЛУШКА ДЛЯ ОТКРЫТИЯ ЧАТА С ПОЛЬЗОВАТЕЛЕМ #$userId]",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun setResult(requestKey: String, result: Bundle) {
        parentFragmentManager.setFragmentResult(requestKey, result)
    }
}