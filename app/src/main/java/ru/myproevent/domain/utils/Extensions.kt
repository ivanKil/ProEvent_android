package ru.myproevent.domain.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.domain.models.entities.Status

fun ImageView.load(url: String){
    Glide.with(context).load(url).into(this)
}

fun ProfileDto.toContact(status: Status?) =
    Contact(userId, status, fullName, nickName, msisdn, position, birthdate, imgUri, description)