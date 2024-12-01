package com.example.movieratingapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val isAuthenticated = MutableLiveData<Boolean>().apply { value = false }
}
