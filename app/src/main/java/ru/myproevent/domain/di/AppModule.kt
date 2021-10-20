package ru.myproevent.domain.di

import dagger.Module
import dagger.Provides
import ru.myproevent.ProEventApp

@Module
class AppModule(val app: ProEventApp) {
    @Provides
    fun provideApp(): ProEventApp {
        return app
    }
}