package ru.myproevent.ui.presenters.code

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

class CodePresenter : MvpPresenter<CodeView>() {
    private inner class VerificationObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.newRootScreen(screens.login())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if(error is retrofit2.adapter.rxjava2.HttpException){
                Toast.makeText(ProEventApp.instance, "Ошибка ${error.code()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    private var disposables: CompositeDisposable = CompositeDisposable()

    // TODO: вынести в Dagger
    private var screens: IScreens = Screens()

    fun continueRegistration(code: Int){
        // TODO: спросить у дизайнера нужено ли здесь отображать progress bar
        disposables.add(
            loginRepository
                .verificate(loginRepository.getEmail()!!, code)
                // TODO: вынести AndroidSchedulers.mainThread() в Dagger
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(VerificationObserver())
        )
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}