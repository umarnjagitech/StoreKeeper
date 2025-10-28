package com.umarndungotech.storekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umarndungotech.storekeeper.data.dao.ProductDao
import com.umarndungotech.storekeeper.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productDao: ProductDao) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    init {
        loadAllProducts()
    }

    /** Load all products for dashboard */
    private fun loadAllProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }
    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }

    /** Load one product for detail/update screen */
    fun loadProductById(id: Int) {
        viewModelScope.launch {
            _selectedProduct.value = productDao.getProductById(id)
        }
    }

    /** Normal CRUD operations */
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            productDao.insert(product)
            loadAllProducts() // refresh UI immediately
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productDao.update(product)
            loadProductById(product.id) // refresh detail screen if open
            loadAllProducts()           // refresh dashboard
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productDao.delete(product)
            _selectedProduct.value = null
            loadAllProducts() // refresh dashboard
        }
    }
}
