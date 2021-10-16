package ru.myproevent.ui.screens

import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun authorization(): Screen
    fun home(): Screen
    fun settings(): Screen
}