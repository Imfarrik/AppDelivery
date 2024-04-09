package com.ccinc.test

import android.app.Application
import com.ccinc.database.foodDatabase
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
    }
}