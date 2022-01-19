package ru.myproevent.domain.models.repositories.images

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface IImagesRepository {
    fun saveImage(file: File): Call<ResponseBody>
    fun getImage(uuid: String): Single<File>
    fun deleteImage(uuid: String): Completable
}
