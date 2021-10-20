package ru.myproevent.domain.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Singleton

@Module
class CiceroneModule {

    var cicerone: Cicerone<Router> = Cicerone.create()

    @Singleton
    @Provides
    fun provideCicerone(): Cicerone<Router> = cicerone

    @Singleton
    @Provides
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.getNavigatorHolder()

    @Singleton
    @Provides
    fun provideRouter(): Router = cicerone.router

    @Singleton
    @Provides
    fun provideScreens(): IScreens = Screens()
}