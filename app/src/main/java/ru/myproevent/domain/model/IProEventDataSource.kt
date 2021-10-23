package ru.myproevent.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IProEventDataSource {
    @POST("api/v1/auth/login")
    fun getToken(@Body loginBody: JSONObject): Call<LoginResponse?>?
}

@Parcelize
data class LoginBody(val email: String, val password: String) : Parcelable

@Parcelize
data class LoginResponse(val token: String) : Parcelable
