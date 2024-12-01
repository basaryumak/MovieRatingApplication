package com.example.movieratingapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Dynamically update menu
        updateBottomNavigationMenu(bottomNavigationView)
    }

    private fun updateBottomNavigationMenu(bottomNavigationView: BottomNavigationView) {
        val menu = bottomNavigationView.menu
        menu.clear()

        if (isAuthenticated()) {
            menu.add(0, R.id.movieRecyclerFragment, 0, "Movies")
            menu.add(0, R.id.profileFragment, 1, "Profile")
        } else {
            menu.add(0, R.id.loginFragment, 0, "Login")
        }
    }

    private fun isAuthenticated(): Boolean {
        // Replace with logic to check user authentication, e.g., SharedPreferences or ViewModel
        return false // Change based on your auth logic
    }
}
