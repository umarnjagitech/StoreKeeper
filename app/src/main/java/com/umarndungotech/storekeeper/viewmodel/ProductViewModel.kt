package com.umarndungotech.storekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umarndungotech.storekeeper.data.dao.ProductDao
import com.umarndungotech.storekeeper.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productDao: ProductDao) : ViewModel() {

    val products: Flow<List<Product>> = productDao.getAllProducts()

    private val _insertionSuccess = MutableStateFlow<Boolean?>(null)
    val insertionSuccess: StateFlow<Boolean?> = _insertionSuccess

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            productDao.insert(product)
            _insertionSuccess.value = true
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

    fun getProductById(id: Int): Flow<Product?> {
        return productDao.getProductById(id)
    }
}
