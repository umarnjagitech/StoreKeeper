package com.umarndungotech.storekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umarndungotech.storekeeper.data.dao.UserDao
import com.umarndungotech.storekeeper.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val _signupSuccess = MutableStateFlow<Boolean?>(null)
    val signupSuccess: StateFlow<Boolean?> = _signupSuccess

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                _errorMessage.value = "User already exists!"
                _signupSuccess.value = false
            } else {
                userDao.insert(User(name = name, email = email, password = password))
                _signupSuccess.value = true
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                _loginSuccess.value = true
            } else {
                _errorMessage.value = "Invalid email or password"
                _loginSuccess.value = false
            }
        }
    }
}
