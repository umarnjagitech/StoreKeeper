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
                // Image is saved to the temporary URI, now copy it to internal storage
                val tempUri = Uri.parse(productImageUri)
                val newUri = saveImageToInternalStorage(context, tempUri)
                productImageUri = newUri.toString()
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
                    if (productName.isNotBlank() && productQuantity.isNotBlank() && productPrice.isNotBlank()) {
                        val product = Product(
                            name = productName,
                            quantity = productQuantity.toInt(),
                            price = productPrice.toDouble(),
                            imageUri = productImageUri
                        )
                        productViewModel.insertProduct(product)
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
                        val photoFile = createImageFile(context)
                        val photoUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            photoFile
                        )
                        productImageUri = photoUri.toString()
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
    }
}

fun createImageFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

fun saveImageToInternalStorage(context: Context, tempUri: Uri): Uri {
    val inputStream = context.contentResolver.openInputStream(tempUri)
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val outputStream = context.openFileOutput("product_image_${timeStamp}.jpg", Context.MODE_PRIVATE)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return Uri.fromFile(File(context.filesDir, "product_image_${timeStamp}.jpg"))
}
