package com.hearx.hearingtest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hearx.hearingtest.data.local.entity.HearingTestResult

@Dao
interface HearingTestResultDao {

    @Insert
    suspend fun insert(result: HearingTestResult)

    @Query("SELECT * FROM hearingtestresult ORDER BY score DESC")
    suspend fun getAll(): List<HearingTestResult>
}