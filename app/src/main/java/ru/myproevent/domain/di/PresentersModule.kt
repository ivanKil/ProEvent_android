package ru.myproevent.domain.di

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.myproevent.ui.presenters.authorization.AuthorizationPresenter
import ru.myproevent.ui.presenters.home.HomePresenter
import ru.myproevent.ui.presenters.main.MainPresenter
import ru.myproevent.ui.presenters.presenter.SettingsPresenter
import ru.myproevent.ui.screens.IScreens

@Module
class PresentersModule {
    @Provides
    fun providesMainPresenter(
        router: Router,
        screens: IScreens
    ): MainPresenter {
        return MainPresenter(router)
    }

    @Provides
    fun providesAuthorizationPresenter(
        router: Router,
        screens: IScreens
    ): AuthorizationPresenter {
        return AuthorizationPresenter(router)
    }

    @Provides
    fun providesHomePresenter(
        router: Router,
        screens: IScreens
    ): HomePresenter {
        return HomePresenter(router)
    }

    @Provides
    fun providesSettingsPresenter(
        router: Router,
        screens: IScreens
    ): SettingsPresenter {
        return SettingsPresenter(router)
    }
}