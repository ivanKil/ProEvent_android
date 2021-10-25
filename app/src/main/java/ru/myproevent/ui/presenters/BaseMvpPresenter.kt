package ru.myproevent.ui.presenters

import com.github.terrakok.cicerone.Router
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView
import ru.myproevent.ui.screens.IScreens
import javax.inject.Inject

open class BaseMvpPresenter<V : MvpView> : MvpPresenter<V>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    protected var compositeDisposable = CompositeDisposable()

    protected fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}