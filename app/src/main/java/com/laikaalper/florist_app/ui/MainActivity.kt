package com.laikaalper.florist_app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.laikaalper.florist_app.MainApplication
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shared = this

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE

                }

                R.id.SearchFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE

                }

                R.id.CartFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE

                }

                R.id.FavoritesFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE

                }

                else -> binding.bottomNav.visibility = View.GONE
            }
        }

    }


    override fun onDestroy() {
        shared = null
        super.onDestroy()
    }

    companion object {

        var shared : MainActivity? = null

        fun navigate(id:Int, args:Bundle?=null) {
            shared?.navController?.navigate(id,args)
        }

    }
}
