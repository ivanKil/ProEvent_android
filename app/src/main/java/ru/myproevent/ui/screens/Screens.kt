package ru.myproevent.ui.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.myproevent.ui.fragments.*

class Screens : IScreens {
    override fun authorization() = FragmentScreen { AuthorizationFragment.newInstance() }
    override fun home() = FragmentScreen { HomeFragment.newInstance() }
    override fun settings() = FragmentScreen { SettingsFragment.newInstance() }
    override fun registration() = FragmentScreen { RegistrationFragment.newInstance() }
    override fun code() = FragmentScreen { CodeFragment.newInstance() }
    override fun login() = FragmentScreen { LoginFragment.newInstance() }
    override fun recovery() = FragmentScreen { RecoveryFragment.newInstance() }
}