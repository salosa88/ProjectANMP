package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ubaya.projectanmp.R
import com.ubaya.projectanmp.databinding.ActivityMainBinding
import com.ubaya.projectanmp.util.Notif

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BottomNavigation ↔ NavController
        val nav = supportFragmentManager
            .findFragmentById(R.id.mainNavHost) as NavHostFragment
        binding.bottomNav.setupWithNavController(nav.navController)

        // Buat channel notifikasi sekali
        Notif.createChannel(this)
    }
}