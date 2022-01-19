package ru.myproevent.domain.models.repositories.images

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import ru.myproevent.domain.models.IProEventDataSource
import java.io.File
import javax.inject.Inject
import android.R.id
import okhttp3.*


class ImagesRepository @Inject constructor(private val api: IProEventDataSource) :
    IImagesRepository {
    override fun saveImage(file: File): Call<ResponseBody> {
        val filePart = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", "image", filePart)

//        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//// MultipartBody.Part is used to send also the actual file name
//        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//// add another part within the multipart request
//        val fullName = RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name")

        return api.saveImage("file", multipartBody)
    }

    override fun getImage(uuid: String): Single<File> {
        return api.getImage(uuid)
    }

    override fun deleteImage(uuid: String): Completable {
        return api.deleteImage(uuid)
    }
}
