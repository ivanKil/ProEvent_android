package ru.myproevent.domain.models.entities

import java.util.*


data class Event(
    val id: Long,
    val name: String,
    val ownerUserId: Long,
    val status: Status,
    val startDate: Date,
    val endDate: Date,
    val description: String?,
    val participantsUserIds: LongArray?,
    val city: String?,
    val address: String?,
    val mapsFileIds: LongArray?,
    val pointsPointIds: LongArray?,
    val imageFile: String?,
){
    enum class Status(val value: String) {
        ALL("ALL"),
        ACTUAL("ACTUAL"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED");
        companion object { fun fromString(status: String) = valueOf(status) }
    }
}