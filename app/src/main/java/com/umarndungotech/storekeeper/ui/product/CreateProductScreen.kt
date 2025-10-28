package com.umarndungotech.storekeeper.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.painterResource
import com.umarndungotech.storekeeper.R


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.rememberAsyncImagePainter

// ... (imports)

import android.os.Environment
import android.net.Uri
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.umarndungotech.storekeeper.data.model.Product
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ... (imports)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productImageUri by remember { mutableStateOf<String?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue with taking a picture.
                val photoFile = createImageFile(context)
                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                productImageUri = photoUri.toString()
                takePictureLauncher.launch(photoUri)
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied.
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            productImageUri = uri?.toString()
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // The image is already saved to the URI provided
            }
        }
    )

    LaunchedEffect(Unit) {
        productViewModel.insertionSuccess.collectLatest { isSuccess ->
            if (isSuccess == true) {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Product") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = productQuantity,
                onValueChange = { productQuantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = productPrice,
                onValueChange = { productPrice = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Image(
                painter = if (productImageUri == null) {
                    painterResource(id = R.drawable.placeholder_image)
                } else {
                    rememberAsyncImagePainter(model = Uri.parse(productImageUri))
                },
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Button(
                onClick = { showImageSourceDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Image")
            }

            Button(
                onClick = {
                    val quantity = productQuantity.toIntOrNull()
                    val price = productPrice.toDoubleOrNull()

                    if (productName.isNotBlank() && quantity != null && price != null) {
                        val product = Product(
                            name = productName,
                            quantity = quantity,
                            price = price,
                            imageUri = productImageUri
                        )
                        productViewModel.insertProduct(product)
                    } else {
                        // Optionally, show an error message to the user
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Product")
            }
        }

        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Select Image Source") },
                text = { Text("Choose whether to take a new photo or select from your gallery.") },
                confirmButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }) { Text("Take Photo") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        galleryLauncher.launch("image/*")
                    }) { Text("Choose from Gallery") }
                }
            )
        }
    }
}




