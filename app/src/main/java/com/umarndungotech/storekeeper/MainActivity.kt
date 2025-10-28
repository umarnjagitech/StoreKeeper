package com.umarndungotech.storekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.umarndungotech.storekeeper.data.AppDatabase
import com.umarndungotech.storekeeper.navigation.AppNavHost
import com.umarndungotech.storekeeper.ui.theme.StorekeeperTheme
import com.umarndungotech.storekeeper.viewmodel.AuthViewModel
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel
import com.umarndungotech.storekeeper.viewmodel.ViewModelFactory

import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ask for camera permission
        permissionLauncher.launch(android.Manifest.permission.CAMERA)

        setContent {
            StorekeeperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val database = AppDatabase.getInstance(applicationContext)
                    val viewModelFactory = ViewModelFactory(database.userDao(), database.productDao())
                    val authViewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
                    val productViewModel = ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)
                    AppNavHost(navController = navController, authViewModel = authViewModel, productViewModel = productViewModel)
                }
            }
        }
    }
}