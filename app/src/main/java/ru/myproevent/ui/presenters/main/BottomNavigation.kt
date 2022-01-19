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
     *
     * По этим причинам рекомендую во фрагментах вызывать эту функцию только в override fun onStart().
     */
    fun hideBottomNavigation()

    /**
     * Если эту функцию вызывает экран, то вызов желательно производить перед или сразу после super.onCreateView.
     * Иначе анимации переходов между экранами могут работать некорректно, так как BottomNavigation
     * будет появлятся отдельно, полсе появляения экрана.
     *
     * По этим причинам рекомендую во фрагментах вызывать эту функцию только в override fun onStop().
     */
    fun showBottomNavigation()
}