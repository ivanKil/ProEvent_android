package ru.myproevent.domain.model

import android.os.Parcelable
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import retrofit2.http.Body
import retrofit2.http.POST

interface IProEventDataSource {
    @POST("login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse?>
}

@Parcelize
data class LoginBody(val email: String, val password: String) : Parcelable

@Parcelize
data class LoginResponse(val token: String) : Parcelable

@Parcelize
data class RefreshCheckCodeBody(val email: String) : Parcelable

