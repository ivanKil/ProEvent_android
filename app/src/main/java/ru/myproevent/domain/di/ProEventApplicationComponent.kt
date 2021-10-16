package ru.myproevent.domain.di

import android.content.Context
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.myproevent.ProEventApp

@Component(
    modules = [
        AndroidInjectionModule::class,
        ProEventApplicationModule::class]
)
interface ProEventApplicationComponent : AndroidInjector<ProEventApp> {

    fun proEventScreensComponent(): ProEventScreensComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withContext(context: Context): Builder

        @BindsInstance
        fun withRouter(router: Router): Builder

        @BindsInstance
        fun withNavigatorHolder(navigatorHolder: NavigatorHolder): Builder

        fun build(): ProEventApplicationComponent
    }
}