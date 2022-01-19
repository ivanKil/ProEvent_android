package ru.myproevent.ui.presenters.main

interface BottomNavigation {
    fun openTab(tab: Tab)
    fun exit()

    /**
     * Экран, который вызывает эту функцию, должен вызывать showBottomNavigation после того как он закроется.
     * Иначе BottomNavigation может стать для пользователя недоступным, так как он будет скрыт.
     * При этом showBottomNavigation желательно вызывать после localRouter.exit().
     * Иначе анимации переходов между экранами могут работать некорректно, так как BottomNavigation
     * будет скрываться отдельно, до полного закрытия экрана.
     */
    fun hideBottomNavigation()

    /**
     * Если эту функцию вызывает экран, то вызов желательно производить сразу после super.onCreateView.
     * Иначе анимации переходов между экранами могут работать некорректно, так как BottomNavigation
     * будет появлятся отдельно, полсе появляения экрана.
     */
    fun showBottomNavigation()
}