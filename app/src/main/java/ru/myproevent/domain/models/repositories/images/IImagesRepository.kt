package ru.myproevent.domain.models.repositories.images

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

interface IImagesRepository {
    fun saveImage(file: File): Single<String>
    fun getImage(uuid: String): Single<File>
    fun deleteImage(uuid: String): Completable
}
