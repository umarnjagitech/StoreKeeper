package com.umarndungotech.storekeeper.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productId: Int
) {
    val scope = rememberCoroutineScope()
    var product by remember { mutableStateOf<com.umarndungotech.storekeeper.data.model.Product?>(null) }

    LaunchedEffect(productId) {
        product = productViewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Product Details") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("update_product/$productId")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Update Product")
                    }
                    IconButton(onClick = {
                        scope.launch {
                            product?.let { productViewModel.deleteProduct(it) }
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Product")
                    }
                }
            )
        }
    ) { padding ->
        product?.let { prod ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = prod.imageUri ?: ""),
                    contentDescription = prod.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Name: ${prod.name}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Quantity: ${prod.quantity}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Price: KES ${prod.price}", style = MaterialTheme.typography.bodyMedium)
            }
        } ?: Text(
            text = "Loading...",
            modifier = Modifier.padding(16.dp)
        )
    }
}
