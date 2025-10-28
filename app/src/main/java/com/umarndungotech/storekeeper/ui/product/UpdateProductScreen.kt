package com.umarndungotech.storekeeper.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel
import com.umarndungotech.storekeeper.data.model.Product
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.umarndungotech.storekeeper.R

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.rememberAsyncImagePainter

import android.net.Uri
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Environment

// ... (imports)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productId: Int
) {
    val context = LocalContext.current
    val product by productViewModel.getProductById(productId).collectAsState(initial = null)

    product?.let { prod ->

        var name by remember { mutableStateOf(prod.name) }
        var quantity by remember { mutableStateOf(prod.quantity.toString()) }
        var price by remember { mutableStateOf(prod.price.toString()) }
        var imageUri by remember { mutableStateOf(prod.imageUri) }
        var showImageSourceDialog by remember { mutableStateOf(false) }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                imageUri = uri?.toString()
            }
        )

        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    // Image is saved to the temporary URI, now copy it to internal storage
                    val tempUri = Uri.parse(imageUri)
                    val newUri = saveImageToInternalStorage(context, tempUri)
                    imageUri = newUri.toString()
                }
            }
        )

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Update Product") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = if (imageUri == null) {
                        painterResource(id = R.drawable.placeholder_image)
                    } else {
                        rememberAsyncImagePainter(model = Uri.parse(imageUri))
                    },
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showImageSourceDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val updatedProduct = prod.copy(
                            name = name,
                            quantity = quantity.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0,
                            imageUri = imageUri
                        )
                        productViewModel.updateProduct(updatedProduct)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Product")
                }
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
                        val photoFile = createImageFile(context)
                        val photoUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            photoFile
                        )
                        imageUri = photoUri.toString()
                        takePictureLauncher.launch(photoUri)
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
    } ?: Text("Loading...", modifier = Modifier.padding(16.dp))
}

// Re-using the helper functions from CreateProductScreen.kt
// fun createImageFile(context: Context): File { ... }
// fun saveImageToInternalStorage(context: Context, tempUri: Uri): Uri { ... }
