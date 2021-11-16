package ru.myproevent.ui.activity

//import android.content.Context
//import android.content.res.ColorStateList
//import android.os.Bundle
//import android.view.View.GONE
//import android.view.View.VISIBLE
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentTransaction
//import com.github.terrakok.cicerone.Navigator
//import com.github.terrakok.cicerone.NavigatorHolder
//import com.github.terrakok.cicerone.androidx.AppNavigator
//import io.github.inflationx.viewpump.ViewPumpContextWrapper
//import moxy.MvpAppCompatActivity
//import moxy.ktx.moxyPresenter
//import ru.myproevent.ProEventApp
//import ru.myproevent.R
//import ru.myproevent.databinding.ActivityMainBinding
//import ru.myproevent.ui.BackButtonListener
//import ru.myproevent.ui.presenters.main.MainPresenter
//import ru.myproevent.ui.presenters.main.MainView
//import ru.myproevent.ui.presenters.main.Menu
//import javax.inject.Inject
//
//class MainActivityOld : MvpAppCompatActivity(), MainView {
//
//    private val navigator: Navigator = object : AppNavigator(this, R.id.container) {
//        override fun setupFragmentTransaction(
//            fragmentTransaction: FragmentTransaction,
//            currentFragment: Fragment?,
//            nextFragment: Fragment?
//        ) {
//            setVerticalTransitionAnimation(
//                currentFragment,
//                nextFragment,
//                fragmentTransaction
//            )
//        }
//    }
//
//    private fun setVerticalTransitionAnimation(
//        currFragment: Fragment?,
//        nextFragment: Fragment?,
//        fragmentTransaction: FragmentTransaction
//    ) {
//        fragmentTransaction.setCustomAnimations(
//            R.anim.enter_from_bottom,
//            R.anim.exit_to_bottom,
//            R.anim.enter_from_bottom,
//            R.anim.exit_to_bottom
//        )
//    }
//
//    @Inject
//    lateinit var navigatorHolder: NavigatorHolder
//
//    private val presenter by moxyPresenter {
//        MainPresenter().apply {
//            ProEventApp.instance.appComponent.inject(this)
//        }
//    }
//
//    private var _view: ActivityMainBinding? = null
//    private val view get() = _view!!
//
//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.Theme_Proevent_NoActionBar)
//        super.onCreate(savedInstanceState)
//        defaultColorState = ColorStateList(
//            arrayOf(intArrayOf()),
//            intArrayOf(applicationContext.getColor(R.color.ProEvent_blue_300))
//        )
//        activeColorState = ColorStateList(
//            arrayOf(intArrayOf()),
//            intArrayOf(applicationContext.getColor(R.color.ProEvent_bright_orange_300))
//        )
//        _view = ActivityMainBinding.inflate(layoutInflater).apply {
//            // TODO: отрефакторить
//            home.setOnClickListener { presenter.openHome() }
//            homeHitArea.setOnClickListener { home.performClick() }
//            contacts.setOnClickListener { presenter.openContacts() }
//            contactsHitArea.setOnClickListener { contacts.performClick() }
//            chat.setOnClickListener { presenter.openChat() }
//            chatHitArea.setOnClickListener { chat.performClick() }
//            events.setOnClickListener { presenter.openEvents() }
//            eventsHitArea.setOnClickListener { events.performClick() }
//            settings.setOnClickListener { presenter.openSettings() }
//            settingsHitArea.setOnClickListener { settings.performClick() }
//        }
//        setContentView(view.root)
//        ProEventApp.instance.appComponent.inject(this)
//    }
//
//    override fun onResumeFragments() {
//        super.onResumeFragments()
//        navigatorHolder.setNavigator(navigator)
//    }
//
//    override fun onPause() {
//        navigatorHolder.removeNavigator()
//        super.onPause()
//    }
//
//    override fun hideBottomNavigation() {
//        if (view.bottomNavigation.visibility == GONE) {
//            return
//        }
//        view.bottomNavigation.visibility = GONE
//    }
//
//    override fun showBottomNavigation() {
//        if (view.bottomNavigation.visibility == VISIBLE) {
//            return
//        }
//        view.bottomNavigation.visibility = VISIBLE
//    }
//
//    private lateinit var defaultColorState: ColorStateList
//    private lateinit var activeColorState: ColorStateList
//
//    override fun selectItem(menu: Menu) {
//        presenter.itemSelected(menu)
//        with(view) {
//            home.backgroundTintList = defaultColorState
//            contacts.backgroundTintList = defaultColorState
//            chat.backgroundTintList = defaultColorState
//            events.backgroundTintList = defaultColorState
//            settings.backgroundTintList = defaultColorState
//
//            when (menu) {
//                Menu.HOME -> home.backgroundTintList = activeColorState
//                Menu.CONTACTS -> contacts.backgroundTintList = activeColorState
//                Menu.CHAT -> chat.backgroundTintList = activeColorState
//                Menu.EVENTS -> events.backgroundTintList = activeColorState
//                Menu.SETTINGS -> settings.backgroundTintList = activeColorState
//            }
//        }
//        showBottomNavigation()
//    }
//
//    override fun onBackPressed() {
//        supportFragmentManager.fragments.forEach {
//            if (it is BackButtonListener && it.backPressed()) {
//                return
//            }
//        }
//        presenter.backPressed()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _view = null
//    }
//}
