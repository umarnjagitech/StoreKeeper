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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productId: Int
) {
    val selectedProduct by productViewModel.selectedProduct.collectAsState()

    LaunchedEffect(productId) {
        productViewModel.loadProductById(productId)
    }

    selectedProduct?.let { product ->

        var name by remember { mutableStateOf(product.name) }
        var quantity by remember { mutableStateOf(product.quantity.toString()) }
        var price by remember { mutableStateOf(product.price.toString()) }

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
                    painter = painterResource(id = R.drawable.placeholder_image),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

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
                        val updatedProduct = product.copy(
                            name = name,
                            quantity = quantity.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0
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
    } ?: Text("Loading...", modifier = Modifier.padding(16.dp))
}
