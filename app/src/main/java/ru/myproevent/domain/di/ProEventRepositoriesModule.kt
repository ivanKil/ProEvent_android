package ru.myproevent.domain.di

import dagger.Binds
import dagger.Module
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.domain.model.repositories.internet_access_info.InternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.proevent_login.ProEventLoginRepository
import javax.inject.Singleton

@Module
interface ProEventRepositoriesModule {
    @Singleton
    @Binds
    fun bindLoginRepository(proEventLoginRepository: ProEventLoginRepository): IProEventLoginRepository

    @Singleton
    @Binds
    fun bindInternetAccessInfoRepository(internetAccessInfoRepository: InternetAccessInfoRepository): IInternetAccessInfoRepository
}