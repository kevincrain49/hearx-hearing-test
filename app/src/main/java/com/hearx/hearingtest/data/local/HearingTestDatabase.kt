package com.hearx.hearingtest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hearx.hearingtest.data.local.dao.HearingTestResultDao
import com.hearx.hearingtest.data.local.entity.HearingTestResult

@Database(entities = [HearingTestResult::class], version = 1)
abstract class HearingTestDatabase : RoomDatabase() {

    abstract fun hearingTestResultDao(): HearingTestResultDao

}