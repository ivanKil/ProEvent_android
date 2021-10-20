package ru.myproevent.domain.di

import dagger.Component
import ru.myproevent.ui.activity.MainActivity
import ru.myproevent.ui.presenters.authorization.AuthorizationPresenter
import ru.myproevent.ui.presenters.code.CodePresenter
import ru.myproevent.ui.presenters.home.HomePresenter
import ru.myproevent.ui.presenters.login.LoginPresenter
import ru.myproevent.ui.presenters.main.MainPresenter
import ru.myproevent.ui.presenters.recovery.RecoveryPresenter
import ru.myproevent.ui.presenters.registration.RegistrationPresenter
import ru.myproevent.ui.presenters.settings.SettingsPresenter
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        ProEventApiModule::class,
        ProEventRepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(mainPresenter: MainPresenter)
    fun inject(authorizationPresenter: AuthorizationPresenter)
    fun inject(codePresenter: CodePresenter)
    fun inject(homePresenter: HomePresenter)
    fun inject(loginPresenter: LoginPresenter)
    fun inject(recoveryPresenter: RecoveryPresenter)
    fun inject(registrationPresenter: RegistrationPresenter)
    fun inject(settingsPresenter: SettingsPresenter)
}