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
package com.zqx.imoocmusicdemo.helper

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.blankj.utilcode.util.LogUtils

/**
 * Helper class for a MediaBrowser that handles connecting, disconnecting,
 * and basic browsing with simplified callbacks.
 */
open class MediaBrowserHelper(
    private val mContext: Context,
    private val mMediaBrowserServiceClass: Class<out MediaBrowserServiceCompat?>
) {

    private val mCallbackList: MutableList<MediaControllerCompat.Callback> =
        ArrayList()
    private val mMediaBrowserConnectionCallback: MediaBrowserConnectionCallback =
        MediaBrowserConnectionCallback()
    private val mMediaControllerCallback: MediaControllerCallback = MediaControllerCallback()
    private val mMediaBrowserSubscriptionCallback: MediaBrowserSubscriptionCallback =
        MediaBrowserSubscriptionCallback()
    private var mMediaBrowser: MediaBrowserCompat? = null
    private var mMediaController: MediaControllerCompat? = null

    fun onStart() {
        if (mMediaBrowser == null) {
            mMediaBrowser = MediaBrowserCompat(
                mContext,
                ComponentName(mContext, mMediaBrowserServiceClass),
                mMediaBrowserConnectionCallback,
                null
            )
            mMediaBrowser!!.connect()
        }
        LogUtils.d(
            TAG,
            "onStart: Creating MediaBrowser, and connecting"
        )
    }

    fun onStop() {
        if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mMediaControllerCallback)
            mMediaController = null
        }
        if (mMediaBrowser != null && mMediaBrowser!!.isConnected) {
            mMediaBrowser!!.disconnect()
            mMediaBrowser = null
        }
        resetState()
        LogUtils.d(
            TAG,
            "onStop: Releasing MediaController, Disconnecting from MediaBrowser"
        )
    }

    /**
     * Called after connecting with a [MediaBrowserServiceCompat].
     *
     *
     * Override to perform processing after a connection is established.
     *
     * @param mediaController [MediaControllerCompat] associated with the connected
     * MediaSession.
     */
    protected open fun onConnected(mediaController: MediaControllerCompat) {

    }

    /**
     * Called after loading a browsable [MediaBrowserCompat.MediaItem]
     *
     * @param parentId The media ID of the parent item.
     * @param children List (possibly empty) of child items.
     */
    protected open fun onChildrenLoaded(
        parentId: String,
        children: List<MediaBrowserCompat.MediaItem>
    ) {
    }

    /**
     * Called when the [MediaBrowserServiceCompat] connection is lost.
     */
    protected open fun onDisconnected() {}

    protected open val mediaController: MediaControllerCompat
        get() {
            checkNotNull(mMediaController) { "MediaController is null!" }
            return mMediaController!!
        }

    /**
     * The internal state of the app needs to revert to what it looks like when it started before
     * any connections to the [PlayMusicService2] happens via the [MediaSessionCompat].
     */
    private fun resetState() {
        performOnAllCallbacks(object : CallbackCommand {
            override fun perform(callback: MediaControllerCompat.Callback) {
                callback.onPlaybackStateChanged(null)
            }
        })
        LogUtils.d(TAG, "resetState: ")
    }

    val transportControls: MediaControllerCompat.TransportControls
        get() {
            if (mMediaController == null) {
                LogUtils.d(
                    TAG,
                    "getTransportControls: MediaController is null!"
                )
                throw IllegalStateException("MediaController is null!")
            }
            return mMediaController!!.transportControls
        }

    fun registerCallback(callback: MediaControllerCompat.Callback?) {
        if (callback != null) {
            mCallbackList.add(callback)

            // Update with the latest metadata/playback state.
            if (mMediaController != null) {
                val metadata = mMediaController!!.metadata
                if (metadata != null) {
                    callback.onMetadataChanged(metadata)
                }
                val playbackState = mMediaController!!.playbackState
                if (playbackState != null) {
                    callback.onPlaybackStateChanged(playbackState)
                }
            }
        }
    }

    private fun performOnAllCallbacks(command: CallbackCommand) {
        for (callback in mCallbackList) {
            command.perform(callback)
        }
    }

    /**
     * Helper for more easily performing operations on all listening clients.
     */
    private interface CallbackCommand {
        fun perform(callback: MediaControllerCompat.Callback)
    }

    // Receives callbacks from the MediaBrowser when it has successfully connected to the
    // MediaBrowserService (MusicService).
    private inner class MediaBrowserConnectionCallback :
        MediaBrowserCompat.ConnectionCallback() {
        // Happens as a result of onStart().
        override fun onConnected() {
            try {
                // Get a MediaController for the MediaSession.
                mMediaController = MediaControllerCompat(mContext, mMediaBrowser!!.sessionToken)
                mMediaController!!.registerCallback(mMediaControllerCallback)

                // Sync existing MediaSession state to the UI.
                mMediaControllerCallback.onPlaybackStateChanged(
                    mMediaController!!.playbackState
                )
                this@MediaBrowserHelper.onConnected(mMediaController!!)
            } catch (e: RemoteException) {
                LogUtils.d(
                    TAG,
                    String.format("onConnected: Problem: %s", e.toString())
                )
                throw RuntimeException(e)
            }
            mMediaBrowser!!.subscribe(mMediaBrowser!!.root, mMediaBrowserSubscriptionCallback)
        }
    }

    // Receives callbacks from the MediaBrowser when the MediaBrowserService has loaded new media
    // that is ready for playback.
    inner class MediaBrowserSubscriptionCallback :
        MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            this@MediaBrowserHelper.onChildrenLoaded(parentId, children)
        }
    }

    // Receives callbacks from the MediaController and updates the UI state,
    // i.e.: Which is the current item, whether it's playing or paused, etc.
    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            performOnAllCallbacks(object : CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback) {
                    callback.onMetadataChanged(metadata)
                }
            })
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            performOnAllCallbacks(object : CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback) {
                    callback.onPlaybackStateChanged(state)
                }
            })
        }

        // This might happen if the MusicService is killed while the Activity is in the
        // foreground and onStart() has been called (but not onStop()).
        override fun onSessionDestroyed() {
            resetState()
            onPlaybackStateChanged(null)
            onDisconnected()
        }
    }

    companion object {
        private val TAG = MediaBrowserHelper::class.java.simpleName
    }

}