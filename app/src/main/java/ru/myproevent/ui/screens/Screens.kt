package ru.myproevent.ui.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.myproevent.ui.fragments.AuthorizationFragment
import ru.myproevent.ui.fragments.HomeFragment
import ru.myproevent.ui.fragments.SettingsFragment

class Screens : IScreens {
    override fun authorization() = FragmentScreen { AuthorizationFragment.newInstance() }
    override fun home() = FragmentScreen { HomeFragment.newInstance() }
    override fun settings() = FragmentScreen { SettingsFragment.newInstance() }
}