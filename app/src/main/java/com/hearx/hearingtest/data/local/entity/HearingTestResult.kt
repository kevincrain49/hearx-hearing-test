package com.hearx.hearingtest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HearingTestResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int
)