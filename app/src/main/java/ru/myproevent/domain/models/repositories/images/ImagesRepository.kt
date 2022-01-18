package ru.myproevent.domain.models.repositories.images

import io.reactivex.Completable
import io.reactivex.Single
import ru.myproevent.domain.models.IProEventDataSource
import java.io.File
import javax.inject.Inject

class ImagesRepository @Inject constructor(private val api: IProEventDataSource) :
    IImagesRepository {
    override fun saveImage(file: File): Single<String> {
        return api.saveImage(file)
    }

    override fun getImage(uuid: String): Single<File> {
        return api.getImage(uuid)
    }

    override fun deleteImage(uuid: String): Completable {
        return api.deleteImage(uuid)
    }
}
