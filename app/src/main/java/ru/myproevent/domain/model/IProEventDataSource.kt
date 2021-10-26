package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface IProEventDataSource {
    @POST("login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse?>

    @POST("signup")
    fun signup(@Body signupBody: SignupBody): Single<SignupResponse?>

    @POST("verificationCheckCode")
    fun verificate(@Body verificationBody: VerificationBody): Completable
}

data class LoginBody(val email: String, val password: String)
data class LoginResponse(val token: String)

data class SignupBody(val agreement: Boolean, val email: String, val password: String)
data class SignupResponse(val agreement: Boolean, val email: String, val password: String)

data class VerificationBody(val code: Int, val email: String)
