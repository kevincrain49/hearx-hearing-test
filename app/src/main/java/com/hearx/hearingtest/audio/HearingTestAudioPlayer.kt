package com.hearx.hearingtest.audio

import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

class HearingTestAudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var backgroundNoiseMediaPlayer: MediaPlayer? = null
    private var tripletMediaPlayer: MediaPlayer? = null

    suspend fun play(difficulty: Int, triplet: String) {
        playBackgroundNoise(difficulty)
        playTriplet(triplet)
    }

    private fun playBackgroundNoise(difficulty: Int) {
        val uri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/noise_${difficulty}")
        backgroundNoiseMediaPlayer = MediaPlayer.create(context, uri)
        backgroundNoiseMediaPlayer?.isLooping = true
        backgroundNoiseMediaPlayer?.start()
    }

    private suspend fun playTriplet(triplet: String) {
        repeat(3) { i ->
            delay(1500L)
            tripletMediaPlayer?.release()

            val tripletDigit = triplet[i].digitToInt()
            val uri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/triplet_${tripletDigit}")
            tripletMediaPlayer = MediaPlayer.create(context, uri)
            if (i == 2) {
                tripletMediaPlayer?.setOnCompletionListener {
                    backgroundNoiseMediaPlayer?.release()
                }
            }
            tripletMediaPlayer?.start()
        }
    }

    fun stopPlaying() {
        backgroundNoiseMediaPlayer?.release()
        tripletMediaPlayer?.release()
        backgroundNoiseMediaPlayer = null
        tripletMediaPlayer = null
    }

}