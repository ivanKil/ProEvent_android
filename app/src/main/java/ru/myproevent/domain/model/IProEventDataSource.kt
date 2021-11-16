package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.domain.model.entities.Status

interface IProEventDataSource {
    @POST("auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse?>

    @POST("auth/signup")
    fun signup(@Body signupBody: SignupBody): Single<SignupResponse?>

    @POST("auth/verificationCheckCode")
    fun verificate(@Body verificationBody: VerificationBody): Completable

    @POST("profiles")
    fun createProfile(@Body profile: ProfileDto): Call<ProfileDto>

    @PUT("profiles")
    fun editProfile(@Body profile: ProfileDto): Call<ProfileDto>

    @GET("profiles/user/{userId}")
    fun getProfile(@Path("userId") userId: Long): Call<ProfileDto>

    @GET("contacts")
    fun getContacts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<Page>

    @GET("contacts")
    fun getContacts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: String
    ): Single<Page>

    @POST("contacts/{id}")
    fun addContact(@Path("id") id: Long): Completable

    @DELETE("contacts/{id}")
    fun deleteContact(@Path("id") id: Long): Completable

    @PUT("contacts/accept/{id}")
    fun acceptContact(@Path("id") id: Long): Completable

    @PUT("contacts/decline/{id}")
    fun declineContact(@Path("id") id: Long): Completable
}

data class LoginBody(val email: String, val password: String)
data class LoginResponse(val token: String)

data class SignupBody(val agreement: Boolean, val email: String, val password: String)
data class SignupResponse(val agreement: Boolean, val email: String, val password: String)

data class VerificationBody(val code: Int, val email: String)

data class ProfileDto(
    val userId: Long,
    var fullName: String? = null,
    var nickName: String? = null,
    var msisdn: String? = null,
    var position: String? = null,
    var birthdate: String? = null,
    var imgUri: String? = null,
    var description: String? = null
)

data class ContactDto(val id: Long, val status: String)

data class Page(
    val content: List<ContactDto>,
    val pageable: Pageable,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
)

data class Pageable(
    val offset: Long,
    val pageNumber: Integer,
    val pageSize: Integer,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)

data class Sort(val empty: Boolean, val sorted: Boolean, val unsorted: Boolean)