package ru.myproevent

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import ru.myproevent.domain.di.AppComponent
import ru.myproevent.domain.di.AppModule
import ru.myproevent.domain.di.DaggerAppComponent

class ProEventApp : Application() {
    companion object {
        private var INSTANCE: ProEventApp? = null
        val instance: ProEventApp
            get() = INSTANCE!!
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        // TODO: Вынести в Dagger
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(getString(R.string.default_font))
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}