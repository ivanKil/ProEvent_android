package ru.myproevent.domain.models.repositories.images

import okhttp3.ResponseBody
import retrofit2.Call
import ru.myproevent.domain.models.UUIDBody
import java.io.File

interface IImagesRepository {
    fun saveImage(file: File): Call<UUIDBody>
    fun deleteImage(uuid: String): Call<ResponseBody>
}
