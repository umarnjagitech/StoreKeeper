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
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _selectedProduct.value = productDao.getProductById(id)
        }
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            productDao.insert(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productDao.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productDao.delete(product)
        }
    }
}
