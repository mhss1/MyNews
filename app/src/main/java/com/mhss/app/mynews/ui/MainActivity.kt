package com.mhss.app.mynews.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mhss.app.mynews.R
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import androidx.work.*
import kotlinx.coroutines.launch
import com.mhss.app.mynews.data.wokers.RefreshArticlesWorker
import kotlinx.coroutines.flow.map
import com.mhss.app.mynews.databinding.ActivityMainBinding
import com.mhss.app.mynews.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        var country: String? = null
        lateinit var language: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment, R.id.discoverFragment, R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        readSettings(Constants.COUNTRY_SETTINGS).observe(this){
            if(country == null || country == it) {
                country = if (it == " ") "United States" else it
                setupRefreshWork(ExistingPeriodicWorkPolicy.KEEP)
            }
            else{
                country = if (it == " ") "United States" else it
                setupRefreshWork(ExistingPeriodicWorkPolicy.REPLACE)
            }
        }
        readSettings(Constants.LANGUAGE_SETTINGS).observe(this){
            language = it
        }

    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }
    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun setupRefreshWork(policy: ExistingPeriodicWorkPolicy){
            val inputData = workDataOf("country" to country)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val refreshRequest =
                PeriodicWorkRequestBuilder<RefreshArticlesWorker>(12, TimeUnit.HOURS)
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                Constants.REFRESH_WORKER_NAME,
                policy,
                refreshRequest)
    }

    private fun readSettings(key : String) =
        dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: " "
            }.asLiveData()
}