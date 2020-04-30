/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqx.imoocmusicdemo.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.homepage.MainActivity
import androidx.media.app.NotificationCompat.MediaStyle

/**
 * Keeps track of a notification and updates it automatically for a given MediaSession. This is
 * required so that the music service don't get killed during playback.
 */
class MediaNotificationManager(private val mService: PlayMusicService2) {

    private val mPlayAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.note_btn_play_white,
        mService.getString(R.string.label_play),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_PLAY
        )
    )

    private val mPauseAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.note_btn_pause_white,
        mService.getString(R.string.label_pause),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_PAUSE
        )
    )
    private val mNextAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.note_btn_next_white,
        mService.getString(R.string.label_next),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        )
    )
    private val mPrevAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.note_btn_pre_white,
        mService.getString(R.string.label_previous),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )
    )

    val notificationManager: NotificationManager =
        mService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        notificationManager.cancelAll()
    }

    fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    fun getNotification(
        metadata: MediaMetadataCompat,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token
    ): Notification {
        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val description = metadata.description
        val builder = buildNotification(state, token, isPlaying, description)
        return builder.build()
    }

    private fun buildNotification(
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token,
        isPlaying: Boolean,
        description: MediaDescriptionCompat
    ): NotificationCompat.Builder {

        // Create the (mandatory) notification channel when running on Android Oreo.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val builder =
            NotificationCompat.Builder(
                mService,
                CHANNEL_ID
            )

        builder.setStyle(MediaStyle().run {
            setMediaSession(token)
            setShowActionsInCompactView(
                0,
                1,
                2
            ) // For backwards compatibility with Android L and earlier.
            setShowCancelButton(true)
            setCancelButtonIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    mService,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
        })
            .setColor(ContextCompat.getColor(mService, R.color.notification_bg))
            .setSmallIcon(R.mipmap.logo) // Pending intent that is fired when user clicks on notification.
            .setContentIntent(createContentIntent()) // Title - Usually Song name.
            .setContentTitle(description.title) // Subtitle - Usually Artist name.
            .setContentText(description.subtitle)
            .setLargeIcon(MusicLibrary.getMusicPosterById(description.mediaId!!)) // When notification is deleted (when playback is paused and notification can be
            // deleted) fire MediaButtonPendingIntent with ACTION_STOP.
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    mService, PlaybackStateCompat.ACTION_STOP
                )
            ) // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // If skip to next action is enabled.
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS != 0L) {
            builder.addAction(mPrevAction)
        }
        builder.addAction(if (isPlaying) mPauseAction else mPlayAction)

        // If skip to prev action is enabled.
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT != 0L) {
            builder.addAction(mNextAction)
        }
        return builder
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            val name: CharSequence = "MediaSession"
            // The user-visible description of the channel.
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
            Log.d(
                TAG,
                "createChannel: New channel created"
            )
        } else {
            Log.d(
                TAG,
                "createChannel: Existing channel reused"
            )
        }
    }

    private fun createContentIntent(): PendingIntent {
        val openUI = Intent(mService, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            mService,
            REQUEST_CODE,
            openUI,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    companion object {
        const val NOTIFICATION_ID = 412
        private val TAG = MediaNotificationManager::class.java.simpleName
        private const val CHANNEL_ID = "notify_play_music"
        private const val REQUEST_CODE = 501
    }


}