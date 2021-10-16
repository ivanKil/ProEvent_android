package ru.myproevent.domain.di

import dagger.Module
import dagger.Provides
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Singleton

@Module
class ScreensModule {
    @Singleton
    @Provides
    fun provideScreens(): IScreens
    = Screens()
}