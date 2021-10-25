package ru.myproevent.ui.presenters.login

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

class LoginPresenter : MvpPresenter<LoginView>() {
    private inner class LoginObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.newRootScreen(screens.home())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            Toast.makeText(ProEventApp.instance, "Этого не должно было произойти: $error", Toast.LENGTH_LONG).show()
        }
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    private var disposables: CompositeDisposable = CompositeDisposable()

    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun confirmLogin(){
        if (loginRepository.getLocalEmail() == null || loginRepository.getLocalPassword() == null){
            Toast.makeText(ProEventApp.instance, "Этого не должно было произойти: loginRepository.getLocalEmail() == null || loginRepository.getLocalPassword() == null", Toast.LENGTH_LONG).show()
            return
        }
        // TODO: уточнить у дизайнера нужен ли progress_bar
        disposables.add(
            loginRepository
                .login(loginRepository.getLocalEmail()!!, loginRepository.getLocalPassword()!!)
                // TODO: вынести AndroidSchedulers.mainThread() в Dagger
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(LoginObserver())
        )
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}