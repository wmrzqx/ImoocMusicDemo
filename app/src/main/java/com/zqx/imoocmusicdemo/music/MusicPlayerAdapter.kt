package com.zqx.imoocmusicdemo.music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.blankj.utilcode.util.LogUtils

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.music
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/27
 * @time 22:37
 */
class MusicPlayerAdapter(context: Context, listener: PlaybackInfoListener) :
    PlayerAdapter(context) {

    private val mContext: Context = context.applicationContext
    private var mMusicPath: String? = null
    private val mPlaybackInfoListener: PlaybackInfoListener? = listener
    private var mCurrentMedia: MediaMetadataCompat? = null
    private var mState = 0
    private var mCurrentMediaPlayedToCompletion = false
    private var mMediaPlayer: MediaPlayer? = null

    // Work-around for a MediaPlayer bug related to the behavior of MediaPlayer.seekTo()
    // while not playing.
    private var mSeekWhileNotPlaying = -1

    override fun playFromMedia(metadata: MediaMetadataCompat?) {
        mCurrentMedia = metadata
        playMusic(mCurrentMedia?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)!!)
    }

    override val currentMedia: MediaMetadataCompat? get() = mCurrentMedia

    override val isPlaying: Boolean get() = mMediaPlayer != null && mMediaPlayer!!.isPlaying

    override fun onPlay() {
        if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
            setNewState(PlaybackStateCompat.STATE_PLAYING)
        }
    }

    override fun onPause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            setNewState(PlaybackStateCompat.STATE_PAUSED)
        }
    }

    override fun onStop() {
        setNewState(PlaybackStateCompat.STATE_STOPPED)
        release()
    }

    override fun seekTo(position: Long) {
        mMediaPlayer?.let {
            if (!it.isPlaying) {
                mSeekWhileNotPlaying = position.toInt()
            }
            it.seekTo(position.toInt())

            // Set the state (to the current state) because the position changed and should
            // be reported to clients.
            setNewState(mState)
            LogUtils.d("MusicPlayerAdapter", "拖动进度条到：${position},设置新状态：${mState}")
        }
    }

    override fun setVolume(volume: Float) {
        mMediaPlayer?.setVolume(volume, volume)
    }

    private fun playMusic(musicPath: String) {
        var mediaChanged = mMusicPath == null || musicPath != mMusicPath
        if (mCurrentMediaPlayedToCompletion) {
            mediaChanged = true
            mCurrentMediaPlayedToCompletion = false
        }

        if (!mediaChanged) {
            if (!isPlaying) {
                play()
            } else {
                setNewState(PlaybackStateCompat.STATE_PLAYING)
            }
            return
        } else {
            release()
        }
        mMusicPath = musicPath

        initializeMediaPlayer()

        mMediaPlayer?.run {
            setDataSource(mContext, Uri.parse(musicPath))
            prepareAsync()
            setOnPreparedListener {
                play()
            }
        }
    }

    private fun initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.setOnCompletionListener {
                if (it.currentPosition < mCurrentMedia?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)!!) {
                    return@setOnCompletionListener
                }
                mPlaybackInfoListener?.onPlaybackCompleted()
                setNewState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT)
            }
        }
    }

    private fun setNewState(@PlaybackStateCompat.State newPlayerState: Int) {
        mState = newPlayerState

        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true
        }

        val reportPosition: Long
        if (mSeekWhileNotPlaying >= 0) {
            reportPosition = mSeekWhileNotPlaying.toLong()

            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mSeekWhileNotPlaying = -1
            }
        } else {
            reportPosition =
                if (mMediaPlayer == null) 0 else mMediaPlayer!!.currentPosition.toLong()
        }

        mPlaybackInfoListener?.onPlaybackStateChange(PlaybackStateCompat.Builder().run {
            setActions(getAvailableActions())
            setState(
                mState,
                reportPosition,
                1.0f,
                SystemClock.elapsedRealtime()
            )
            build()
        })
    }

    @PlaybackStateCompat.Actions
    private fun getAvailableActions(): Long {
        var actions = (PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        actions = when (mState) {
            PlaybackStateCompat.STATE_STOPPED -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PAUSE)
            PlaybackStateCompat.STATE_PLAYING -> actions or (PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_SEEK_TO)
            PlaybackStateCompat.STATE_PAUSED -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_STOP)
            else -> actions or (PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE)
        }
        return actions
    }

    private fun release() {
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}