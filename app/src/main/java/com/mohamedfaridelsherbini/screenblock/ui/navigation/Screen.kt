package com.mohamedfaridelsherbini.screenblock.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object AppSelection : Screen("app_selection")
    object Focus : Screen("focus")
    object Summary : Screen("summary")
    object EmergencyContacts : Screen("emergency_contacts")
}
