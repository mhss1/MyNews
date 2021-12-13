package com.mhss.app.mynews.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mhss.app.mynews.R
import androidx.preference.PreferenceManager
import androidx.work.*
import com.mhss.app.mynews.data.wokers.RefreshArticlesWorker
import com.mhss.app.mynews.databinding.ActivityMainBinding
import com.mhss.app.mynews.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferenceManager: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(preferenceManager.getString("theme", ""))

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment, R.id.discoverFragment, R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        setupRefreshWork(ExistingPeriodicWorkPolicy.KEEP)
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }
    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun setupRefreshWork(policy: ExistingPeriodicWorkPolicy){
            val inputData = workDataOf("country" to preferenceManager.getString("country", ""))
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val refreshRequest =
                PeriodicWorkRequestBuilder<RefreshArticlesWorker>(12, TimeUnit.HOURS)
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                Constants.REFRESH_WORKER_NAME,
                policy,
                refreshRequest)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        println("called and key is $key")
        if (key == "theme")
           setTheme(sharedPreferences?.getString(key, ""))
        else if(key == "country")
            setupRefreshWork(ExistingPeriodicWorkPolicy.REPLACE)
    }

    private fun setTheme(theme: String?){
        when (theme) {
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.unregisterOnSharedPreferenceChangeListener(this)
    }
}