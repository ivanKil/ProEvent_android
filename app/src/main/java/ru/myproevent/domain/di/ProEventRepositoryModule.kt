package ru.myproevent.domain.di

import dagger.Binds
import dagger.Module
import ru.myproevent.domain.model.IProEventRepository
import ru.myproevent.domain.model.ProEventRepository
import javax.inject.Singleton

@Module
interface ProEventRepositoryModule {
    @Singleton
    @Binds
    fun bindRepository(proEventRepository: ProEventRepository): IProEventRepository
}