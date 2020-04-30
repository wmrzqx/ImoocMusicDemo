package com.zqx.imoocmusicdemo.music

import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zqx.imoocmusicdemo.bean.MusicBean

class PlayMusicService2 : MediaBrowserServiceCompat() {

    companion object {
        private val TAG = PlayMusicService2::class.java.simpleName
    }

    private lateinit var mSession: MediaSessionCompat
    private var mPlayback: PlayerAdapter? = null

    private var mMediaNotificationManager: MediaNotificationManager? = null
    private var mCallback: MediaSessionCallback? = null
    private var mServiceInStartedState = false

    override fun onCreate() {
        super.onCreate()
        mSession = MediaSessionCompat(this, TAG)
        mCallback = MediaSessionCallback()
        mSession.setCallback(mCallback)
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
        sessionToken = mSession.sessionToken

        mMediaNotificationManager = MediaNotificationManager(this)

        mPlayback = MusicPlayerAdapter(this, MediaPlayerListener())
        LogUtils.d(
            TAG,
            "onCreate: MusicService creating MediaSession, and MediaNotificationManager"
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        MusicLibrary.getMediaItems(this) {
            result.sendResult(it)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MusicLibrary.getRoot(), null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaNotificationManager?.onDestroy()
        mPlayback?.stop()
        mSession.release()
        MusicLibrary.clearMusicPosterBitmap()
        LogUtils.d(
            TAG,
            "onDestroy: MediaPlayerAdapter stopped, and MediaSession released"
        )
    }

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        private val mPlaylist = ArrayList<MediaSessionCompat.QueueItem>()
        private var mQueueIndex = -1
        private var mPreparedMedia: MediaMetadataCompat? = null

        override fun onCustomAction(action: String?, extras: Bundle?) {
            if (action == ACTION_CLEAR_QUEUE) {
                mPlaylist.clear()
            }
        }

        override fun onAddQueueItem(description: MediaDescriptionCompat?) {
            mPlaylist.add(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mQueueIndex == -1) 0 else mQueueIndex
            mSession.setQueue(mPlaylist)
            LogUtils.d(TAG, "添加Music到队列，队列长度为：${mPlaylist.size}")
        }

        override fun onRemoveQueueItem(description: MediaDescriptionCompat?) {
            mPlaylist.remove(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mPlaylist.isEmpty()) -1 else mQueueIndex
            mSession.setQueue(mPlaylist)
        }

        override fun onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()) return
            val mediaId = mPlaylist[mQueueIndex].description.mediaId
            mPreparedMedia = MusicLibrary.getMetadata(mediaId!!)
            mSession.setMetadata(mPreparedMedia)

            if (!mSession.isActive) {
                mSession.isActive = true
            }
        }

        override fun onPlay() {
            if (!isReadyToPlay()) return
            if (mPreparedMedia == null) {
                onPrepare()
            }
            mPlayback?.playFromMedia(mPreparedMedia)
            LogUtils.d(
                TAG,
                "onPlay: MediaSession active"
            )
        }

        override fun onSkipToQueueItem(id: Long) {
            mQueueIndex = id.toInt()
            mPreparedMedia = null
            onPlay()
        }

        override fun onPause() {
            mPlayback?.pause()
        }

        override fun onStop() {
            mPlayback?.stop()
            mSession.isActive = false
        }

        override fun onSkipToNext() {
            LogUtils.d(TAG, "跳到下一首歌")
            mQueueIndex = ++mQueueIndex % mPlaylist.size
            mPreparedMedia = null
            onPlay()
        }

        override fun onSkipToPrevious() {
            mQueueIndex = if (mQueueIndex > 0) mQueueIndex - 1 else mPlaylist.size - 1
            mPreparedMedia = null
            onPlay()
        }

        override fun onSeekTo(pos: Long) {
            mPlayback?.seekTo(pos)
        }

        private fun isReadyToPlay(): Boolean {
            return mPlaylist.isNotEmpty()
        }

    }

    inner class MediaPlayerListener : PlaybackInfoListener() {

        private val mServiceManager: ServiceManager? = ServiceManager()

        override fun onPlaybackStateChange(state: PlaybackStateCompat?) {
            // Report the state to the MediaSession.
            mSession.setPlaybackState(state)

            // Manage the started state of this service.
            when (state!!.state) {
                PlaybackStateCompat.STATE_PLAYING -> mServiceManager!!.moveServiceToStartedState(
                    state
                )
                PlaybackStateCompat.STATE_PAUSED -> mServiceManager!!.updateNotificationForPause(
                    state
                )
                PlaybackStateCompat.STATE_STOPPED -> mServiceManager!!.moveServiceOutOfStartedState(
                    state
                )
            }
        }

        override fun onPlaybackCompleted() {
            mCallback?.onSkipToNext()
        }

        internal inner class ServiceManager {

            internal fun moveServiceToStartedState(state: PlaybackStateCompat) {
                val notification: Notification? =
                    mMediaNotificationManager?.getNotification(
                        mPlayback?.currentMedia!!,
                        state,
                        sessionToken!!
                    )
                if (!mServiceInStartedState) {
                    ContextCompat.startForegroundService(
                        this@PlayMusicService2,
                        Intent(
                            this@PlayMusicService2,
                            PlayMusicService2::class.java
                        )
                    )
                    mServiceInStartedState = true
                }
                startForeground(MediaNotificationManager.NOTIFICATION_ID, notification)
            }

            internal fun updateNotificationForPause(state: PlaybackStateCompat) {
                stopForeground(false)
                val notification: Notification? =
                    mMediaNotificationManager?.getNotification(
                        mPlayback?.currentMedia!!, state, sessionToken!!
                    )
                mMediaNotificationManager?.notificationManager
                    ?.notify(MediaNotificationManager.NOTIFICATION_ID, notification)
            }

            internal fun moveServiceOutOfStartedState(state: PlaybackStateCompat) {
                stopForeground(true)
                stopSelf()
                mServiceInStartedState = false
            }
        }

    }

}
