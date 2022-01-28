package ru.myproevent.domain.models

import android.os.Parcelable
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.myproevent.BuildConfig
import java.io.File

interface IProEventDataSource {
    @POST("auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse?>

    @POST("auth/signup")
    fun signup(@Body signupBody: SignupBody): Single<SignupResponse?>

    @POST("auth/verificationCheckCode")
    fun verificate(@Body verificationBody: VerificationBody): Completable

    @POST("auth/refreshCheckCode")
    fun refreshCheckCode(@Body refreshBody: RefreshBody): Completable

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

    @POST("events")
    fun saveEvent(@Body event: EventDto): Single<EventDto>

    @PUT("events")
    fun editEvent(@Body event: EventDto): Single<EventDto>

    @HTTP(method = "DELETE", path = "events", hasBody = true)
    fun deleteEvent(@Body event: EventDto): Completable

    @GET("events/{eventId}")
    fun getEvent(@Path("eventId") eventId: Long): Single<EventDto>

    @GET("events/user/{userId}")
    fun getEventsForUser(@Path("userId") userId: Long): Single<List<EventDto>>

    @POST("auth/resetPassword")
    fun resetPassword(@Body resetPasswordBody: ResetPasswordBody): Completable

    @POST("auth/newPassword")
    fun setNewPassword(@Body newPasswordBody: NewPasswordBody): Completable

    @Multipart
    @POST("storage")
    fun saveImage(
        @Part("file") name: String,
        @Part image: MultipartBody.Part
    ): Call<UUIDBody>

    @DELETE("storage/{uuid}")
    fun deleteImage(@Path("uuid") uuid: String): Call<ResponseBody>

    @POST
    @Headers(
        "Authorization: Token ${BuildConfig.EMAIL_HINT_API_TOKEN}",
        "Accept: application/json",
        "Content-Type: application/json"
    )
    fun getEmailHint(
        @Body hintRequest: HintRequest,
        @Url fullUrl: String = BuildConfig.EMAIL_HINT_API_URL
    ): Single<HintResponse>
}

data class LoginBody(val email: String, val password: String)
data class LoginResponse(val token: String)

data class SignupBody(val agreement: Boolean, val email: String, val password: String)
data class SignupResponse(val agreement: Boolean, val email: String, val password: String)

data class VerificationBody(val code: Int, val email: String)

data class RefreshBody(val email: String)

data class ResetPasswordBody(val email: String)

data class NewPasswordBody(val code: Int, val email: String, val password: String)

data class UUIDBody(val uuid: String)

@Parcelize
data class ProfileDto(
    var userId: Long,
    var email: String? = null,
    var fullName: String? = null,
    var nickName: String? = null,
    var msisdn: String? = null,
    var position: String? = null,
    var birthdate: String? = null,
    var imgUri: String? = null,
    var description: String? = null
) : Parcelable

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

data class EventDto(
    val id: Long?,
    val name: String,
    val ownerUserId: Long,
    val eventStatus: String,
    val startDate: String,
    val endDate: String,
    val description: String?,
    val participantsUserIds: LongArray?,
    val city: String?,
    val address: String?,
    val mapsFileIds: LongArray?,
    val pointsPointIds: LongArray?,
    val imageFile: String?,
)

data class HintRequest(val query: String)
data class HintResponse(val suggestions: List<Suggestion>)
data class Suggestion(val value: String)
