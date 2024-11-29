package com.example.movieratingapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel : ViewModel() {
    private val _userEmail = MutableLiveData<String>()

    val userEmail: LiveData<String> get() = _userEmail

    fun fetchUser() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        _userEmail.value = user?.email ?: "Unknown Email"
    }
}
