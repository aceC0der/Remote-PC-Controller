package com.dev_ace_coder.pccontrol

import BluetoothSender
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(lgClass: Login){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route){
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController, lgClass)
        }
        composable(route = Screen.NewAccountScreen.route){
            NewAccount(navController, lgClass)
        }
        composable(route = Screen.MainScreen.route){
            MainScreen(navController)
        }
        composable(route = Screen.WifiScreen.route){
            BluetoothSender(navController)
        }
    }
}