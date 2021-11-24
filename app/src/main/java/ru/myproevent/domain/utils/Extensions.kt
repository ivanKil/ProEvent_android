package ru.myproevent.domain.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.myproevent.domain.models.EventDto
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.domain.models.entities.Contact.Status
import ru.myproevent.domain.models.entities.Event
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.load(url: String) {
    Glide.with(context).load(url).into(this)
}

fun ProfileDto.toContact(status: Status?) =
    Contact(userId, status, fullName, nickName, msisdn, position, birthdate, imgUri, description)

fun EventDto.toEvent(datePattern: String = "yyyy-MM-dd'T'HH:mm:ss"): Event {
    val dateFormat = SimpleDateFormat(datePattern)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return Event(
        id,
        name,
        ownerUserId,
        Event.Status.fromString(eventStatus),
        dateFormat.parse(startDate),
        dateFormat.parse(endDate),
        description,
        participantsUserIds,
        city,
        address,
        mapsFileIds,
        pointsPointIds,
        imageFile
    )
}

fun Event.toEventDto(datePattern: String = "yyyy-MM-dd'T'HH:mm:ss"): EventDto {
    val dateFormat = SimpleDateFormat(datePattern)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return EventDto(
        id,
        name,
        ownerUserId,
        eventStatus.value,
        dateFormat.format(startDate),
        dateFormat.format(endDate),
        description,
        participantsUserIds,
        city,
        address,
        mapsFileIds,
        pointsPointIds,
        imageFile
    )
}