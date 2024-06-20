package com.ccinc.test

import android.app.Application
import android.util.Log
import com.ccinc.database.foodDatabase
import com.trlogic.tracker.TrackerManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        CoroutineScope(SupervisorJob()).launch {
            foodDatabase(this@App).basketDao.clean()
        }
        super.onCreate()

        initTracker()

    }

    private fun initTracker() {
        val context = applicationContext

        val TENANT = "tbc"
        val API_URL = "https://poc.riskoffice.formica.ai"
        val API_KEY = "onaphYtIonimItIONceLp"

        try {
            TrackerManager.initialize(context, TENANT, API_URL, API_KEY) {}
            Log.i("Hello","initialized")
        } catch (e: Exception) {
            Log.i("Hello","error")
        }
    }
}