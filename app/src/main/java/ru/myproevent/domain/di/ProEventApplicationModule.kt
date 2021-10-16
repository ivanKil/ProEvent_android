package ru.myproevent.domain.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.myproevent.ui.activity.MainActivity

@Module(
    subcomponents = [
        ProEventScreensComponent::class]
)
interface ProEventApplicationModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

}