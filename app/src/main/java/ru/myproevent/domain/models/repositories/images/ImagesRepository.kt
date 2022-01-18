package ru.myproevent.domain.models.repositories.images

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import ru.myproevent.domain.models.IProEventDataSource
import java.io.File
import javax.inject.Inject

class ImagesRepository @Inject constructor(private val api: IProEventDataSource) :
    IImagesRepository {
    override fun saveImage(file: File): Call<ResponseBody> {
        val filePart = file.asRequestBody("image".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", "image", filePart)
        return api.saveImage("file", multipartBody)
    }

    override fun getImage(uuid: String): Single<File> {
        return api.getImage(uuid)
    }

    override fun deleteImage(uuid: String): Completable {
        return api.deleteImage(uuid)
    }
}
