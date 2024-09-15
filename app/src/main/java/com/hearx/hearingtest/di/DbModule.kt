package com.hearx.hearingtest.di

import android.content.Context
import androidx.room.Room
import com.hearx.hearingtest.data.local.HearingTestDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    companion object {
        const val DB_NAME = "hearing_test_database"
    }

    @Provides
    @Singleton
    fun provideHearingTestDatabase(@ApplicationContext context: Context): HearingTestDatabase {
        return Room.databaseBuilder(
            context,
            HearingTestDatabase::class.java,
            DB_NAME
        ).build()
    }

}