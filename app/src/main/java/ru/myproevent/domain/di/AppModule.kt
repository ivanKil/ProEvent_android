package ru.myproevent.domain.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.myproevent.ProEventApp

@Module
class AppModule(val app: ProEventApp) {

    @Provides
    fun provideApp(): ProEventApp {
        return app
    }

    @Provides
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}