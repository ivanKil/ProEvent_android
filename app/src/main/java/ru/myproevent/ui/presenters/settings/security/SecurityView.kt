package ru.myproevent.ui.presenters.settings.security

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface SecurityView : BaseMvpView {
    fun showProfile(profileDto: ProfileDto)
    fun makeProfileEditable()
}