package ru.myproevent.domain.di

import dagger.Module
import dagger.Provides
import ru.myproevent.domain.models.LocalCiceroneHolder
import javax.inject.Singleton

@Module
object LocalNavigationModule {

    @Provides
    @Singleton
    fun provideLocalNavigationHolder(): LocalCiceroneHolder = LocalCiceroneHolder()
}