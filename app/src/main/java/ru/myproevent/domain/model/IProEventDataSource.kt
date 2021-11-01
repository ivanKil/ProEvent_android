package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface IProEventDataSource {
    @POST("auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse?>

    @POST("auth/signup")
    fun signup(@Body signupBody: SignupBody): Single<SignupResponse?>

    @POST("auth/verificationCheckCode")
    fun verificate(@Body verificationBody: VerificationBody): Completable

    @POST("profiles")
    fun createProfile(@Body profile: ProfileDto): Single<ProfileDto>

    @PUT("profiles")
    fun editProfile(@Body profile: ProfileDto): Single<ProfileDto>

    @GET("profiles/user/{userId}")
    fun getProfile(@Path("userId") userId: Long): Call<ProfileDto>
}

data class LoginBody(val email: String, val password: String)
data class LoginResponse(val token: String)

data class SignupBody(val agreement: Boolean, val email: String, val password: String)
data class SignupResponse(val agreement: Boolean, val email: String, val password: String)

data class VerificationBody(val code: Int, val email: String)

data class ProfileDto(
    val userId: Long,
    val fullName: String?,
    val nickName: String?,
    val msisdn: String?,
    val position: String?,
    val birthdate: String?,
    val imgUri: String?,
    val description: String?
)