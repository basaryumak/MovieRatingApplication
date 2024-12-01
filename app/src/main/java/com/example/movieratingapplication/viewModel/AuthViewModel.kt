package com.example.movieratingapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AuthViewModel : ViewModel() {
    val isAuthenticated = MutableLiveData<Boolean>().apply { value = false }
    val email = MutableLiveData<String>()
    val displayName = MutableLiveData<String>("Hi!") // Default to "Hi!"

}
