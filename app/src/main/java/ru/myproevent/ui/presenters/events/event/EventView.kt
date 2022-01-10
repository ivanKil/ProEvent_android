package ru.myproevent.ui.presenters.events.event

import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Event
import ru.myproevent.ui.presenters.BaseMvpView

@AddToEnd
interface EventView : BaseMvpView {
    fun addParticipantItemView(profileDto: ProfileDto)
    fun enableDescriptionEdit()
    fun expandDescription()
    fun expandMaps()
    fun expandPoints()
    fun expandParticipants()
    fun clearParticipants()
    fun showAbsoluteBar(
        title: String,
        iconResource: Int?,
        iconTintResource: Int?,
        onCollapseScroll: Int,
        onCollapse: () -> Unit,
        onEdit: () -> Unit
    )
    fun hideAbsoluteBar()
    fun unlockNameEdit()
    fun unlockDateEdit()
    fun unlockLocationEdit()
    @OneExecution
    fun cancelEdit()
    fun showEditOptions()
    fun hideEditOptions()
    fun showActionOptions()
    fun lockEdit()
    fun removeParticipant(id: Long, pickedParticipantsIds: List<Long>)
}