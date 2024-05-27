package com.dev_ace_coder.pccontrol

sealed class Screen(val route: String){

    object LoginScreen : Screen("login_screen")
    object NewAccountScreen : Screen("new_account_screen")
    object MainScreen : Screen("Main_screen")
    object WifiScreen : Screen("Wifi_screen")
}
