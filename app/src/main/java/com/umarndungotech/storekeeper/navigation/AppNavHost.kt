package com.umarndungotech.storekeeper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umarndungotech.storekeeper.ui.auth.LoginScreen
import com.umarndungotech.storekeeper.ui.auth.SignUpScreen
import com.umarndungotech.storekeeper.ui.screens.DashboardScreen
import com.umarndungotech.storekeeper.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Signup : AuthScreen("signup")
}

sealed class ProductScreen(val route: String) {
    object Dashboard : ProductScreen("dashboard")
}

@Composable
fun AppNavHost(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.route
    ) {

        composable(AuthScreen.Login.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(AuthScreen.Signup.route) {
            SignUpScreen(navController, authViewModel)
        }

        composable(ProductScreen.Dashboard.route) {
            DashboardScreen(navController)
        }
    }
}
