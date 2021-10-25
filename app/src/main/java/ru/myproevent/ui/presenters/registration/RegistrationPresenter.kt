package ru.myproevent.ui.presenters.registration

import android.widget.Toast
import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import moxy.MvpPresenter
import ru.myproevent.ProEventApp
import ru.myproevent.domain.model.IProEventLoginRepository
import ru.myproevent.ui.screens.IScreens
import ru.myproevent.ui.screens.Screens
import javax.inject.Inject

class RegistrationPresenter : MvpPresenter<RegistrationView>() {
    private inner class SignupObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.navigateTo(screens.code())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if(error is retrofit2.adapter.rxjava2.HttpException){
                if(error.code() == 409){
                    Toast.makeText(ProEventApp.instance, "Для введённого email уже есть аккаунт", Toast.LENGTH_LONG).show()
                    return
                }
                Toast.makeText(ProEventApp.instance, "Ошибка ${error.code()}", Toast.LENGTH_LONG).show()
                return
            }
            Toast.makeText(ProEventApp.instance, "${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    @Inject
    lateinit var router: Router
    // TODO: вынести в Dagger

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    private var disposables: CompositeDisposable = CompositeDisposable()

    private var screens: IScreens = Screens()

    fun signup() {
        router.navigateTo(screens.authorization())

    }

    fun continueRegistration(agreement: Boolean, email: String, password: String) {
        // TODO: спросить у дизайнера нужен ли progress bar
        disposables.add(
            loginRepository
                .signup(agreement, email, password)
                // TODO: вынести AndroidSchedulers.mainThread() в Dagger
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(SignupObserver())
        )
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}