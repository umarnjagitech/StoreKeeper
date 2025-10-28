package com.umarndungotech.storekeeper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umarndungotech.storekeeper.ui.auth.LoginScreen
import com.umarndungotech.storekeeper.ui.auth.SignUpScreen
import com.umarndungotech.storekeeper.viewmodel.AuthViewModel
import com.umarndungotech.storekeeper.ui.product.CreateProductScreen
import com.umarndungotech.storekeeper.ui.product.ProductDashboardScreen
import com.umarndungotech.storekeeper.ui.product.ProductDetailScreen
import com.umarndungotech.storekeeper.ui.product.UpdateProductScreen
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Signup : AuthScreen("signup")
}

sealed class ProductScreen(val route: String) {
    object Dashboard : ProductScreen("dashboard")
    object CreateProduct : ProductScreen("createProduct")
    object ProductDetails : ProductScreen("productDetails/{productId}")
    object UpdateProduct : ProductScreen("updateProduct/{productId}")
}

@Composable
fun AppNavHost(navController: NavHostController, authViewModel: AuthViewModel, productViewModel: ProductViewModel) {

    val loginSuccess by authViewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            navController.navigate(ProductScreen.Dashboard.route) {
                popUpTo(AuthScreen.Login.route) {
                    inclusive = true
                }
            }
        }
    }

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
            ProductDashboardScreen(navController, productViewModel)
        }

        composable(ProductScreen.CreateProduct.route) {
            CreateProductScreen(navController, productViewModel)
        }

        composable(
            route = ProductScreen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(navController, productViewModel, productId)
        }

        composable(
            route = ProductScreen.UpdateProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            UpdateProductScreen(navController, productViewModel, productId)
        }
    }
}
