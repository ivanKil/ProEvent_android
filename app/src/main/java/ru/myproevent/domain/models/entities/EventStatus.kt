package ru.myproevent.domain.models.entities

enum class EventStatus(val value: String) {
    ACTUAL("ACTUAL"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    companion object {
        fun fromString(status: String) = Status.valueOf(status)
    }
}