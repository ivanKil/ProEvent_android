package ru.myproevent.ui.presenters.settings.account

import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEndSingle
interface AccountView : BaseMvpView {
    fun showProfile(profileDto: ProfileDto)
    fun makeProfileEditable()
}
