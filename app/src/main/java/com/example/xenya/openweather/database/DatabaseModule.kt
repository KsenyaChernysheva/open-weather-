package com.example.xenya.openweather.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private var context: Context) {

    companion object {
        private const val DATABASE_NAME = "weather"
    }

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideCityDao(appDatabase: AppDatabase): CityDao = appDatabase.getCityDao()
}
