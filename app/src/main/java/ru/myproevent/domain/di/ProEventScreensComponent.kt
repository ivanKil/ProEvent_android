package ru.myproevent.domain.di

import dagger.Subcomponent
import ru.myproevent.ui.fragments.AuthorizationFragment
import ru.myproevent.ui.fragments.HomeFragment
import ru.myproevent.ui.fragments.SettingsFragment
import javax.inject.Singleton

@Singleton
@Subcomponent(
    modules = [
        ScreensModule::class,
        PresentersModule::class]
)
interface ProEventScreensComponent {

    fun inject(authorizationFragment: AuthorizationFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(settingsFragment: SettingsFragment)

    @Subcomponent.Builder
    interface Builder {

        fun build(): ProEventScreensComponent

    }
}