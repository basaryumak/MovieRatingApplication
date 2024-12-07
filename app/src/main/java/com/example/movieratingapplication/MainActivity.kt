package com.example.movieratingapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movieratingapplication.viewModel.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            updateBottomNavigationMenu(bottomNavigationView, isAuthenticated)
        }
    }

    private fun updateBottomNavigationMenu(
        bottomNavigationView: BottomNavigationView,
        isAuthenticated: Boolean
    ) {
        val menu = bottomNavigationView.menu
        menu.clear()

        if (isAuthenticated) {
            menu.add(0, R.id.movieRecyclerFragment, 0, "Movies").apply {
                setIcon(R.drawable.movie)
            }

            menu.add(0, R.id.profileFragment, 1, "Profile").apply {
                setIcon(R.drawable.user_profile)
            }

        } else {
            menu.add(0, R.id.logout_button, 0, "Log In").apply {
                setIcon(R.drawable.movies)
            }
        }
}}
