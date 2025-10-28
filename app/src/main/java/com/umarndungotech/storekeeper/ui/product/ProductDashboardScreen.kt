package com.umarndungotech.storekeeper.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.umarndungotech.storekeeper.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDashboardScreen(navController: NavController, productViewModel: ProductViewModel) {

    val products by productViewModel.products.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products Dashboard") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createProduct")
            }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (products.isEmpty()) {
                Text("No Product created. Add your first product!")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(products) { product ->
                        ProductCard(
                            name = product.name,
                            quantity = product.quantity,
                            onViewClick = {
                                navController.navigate("productDetails/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(name: String, quantity: Int, onViewClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Name: $name", style = MaterialTheme.typography.titleMedium)
            Text(text = "Quantity: $quantity", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = onViewClick, modifier = Modifier.align(Alignment.End)) {
                Text("View Product")
            }
        }
    }
}
