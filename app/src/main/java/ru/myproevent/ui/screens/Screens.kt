package ru.myproevent.ui.screens

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.ui.fragments.*

class Screens : IScreens {
    override fun authorization() = FragmentScreen { AuthorizationFragment.newInstance() }
    override fun home() = FragmentScreen { HomeFragment.newInstance() }
    override fun settings() = FragmentScreen { SettingsFragment.newInstance() }
    override fun registration() = FragmentScreen { RegistrationFragment.newInstance() }
    override fun code() = FragmentScreen { CodeFragment.newInstance() }
    override fun login() = FragmentScreen { LoginFragment.newInstance() }
    override fun recovery() = FragmentScreen { RecoveryFragment.newInstance() }
    override fun account() = FragmentScreen { AccountFragment.newInstance() }
    override fun security() = FragmentScreen { SecurityFragment.newInstance() }
    override fun contacts() = FragmentScreen { ContactsFragment.newInstance() }
    override fun contact(contact: Contact): Screen = FragmentScreen { ContactFragment.newInstance(contact) }
    override fun contactAdd(): Screen = FragmentScreen { ContactAddFragment.newInstance() }
    override fun chat() = FragmentScreen { HomeFragment.newInstance() }
    override fun events() = FragmentScreen { HomeFragment.newInstance() }
}