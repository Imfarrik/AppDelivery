package com.ccinc.test

import android.content.Context
import com.ccinc.api.FoodApi
import com.ccinc.api.foodApi
import com.ccinc.common.Logger
import com.ccinc.common.androidLogcatLogger
import com.ccinc.database.FoodDatabase
import com.ccinc.database.foodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient? {
        return if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient?): FoodApi {
        return foodApi(
            baseUrl = BuildConfig.API_BASE_URL,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FoodDatabase {
        return foodDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = androidLogcatLogger()
}