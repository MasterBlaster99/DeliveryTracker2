package com.example.deliverytracker.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.deliverytracker.R
import com.example.deliverytracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupNav()

    }

    private fun setupNav() {
        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navigationHost.navController
        navController.setGraph(R.navigation.nav_graph)
        binding?.bottomNavigationView?.let {
            NavigationUI.setupWithNavController(it, navController)
            it.setOnItemSelectedListener { item ->
                navController.navigate(item.itemId)
                true
            }
        }
    }
}