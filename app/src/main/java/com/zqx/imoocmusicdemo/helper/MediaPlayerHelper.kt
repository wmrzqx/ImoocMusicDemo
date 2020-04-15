package com.zqx.imoocmusicdemo.helper

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.LogUtils

/**
 * Created by Administrator on 2020/04/13 6:45.
 */
class MediaPlayerHelper private constructor(context: Context) {
    companion object {
        private var mInstance: MediaPlayerHelper? = null

        fun getInstance(context: Context): MediaPlayerHelper? {
            if (mInstance == null) {
                synchronized(MediaPlayerHelper::class) {
                    if (mInstance == null) {
                        mInstance = MediaPlayerHelper(context.applicationContext)
                    }
                }
            }
            return mInstance
        }
    }

    private var mContext: Context? = null

    private var mMediaPlayer: MediaPlayer? = null

    private var mOnMediaPlayerHelperListener: ((MediaPlayer) -> Unit)? = null

    private var mPath: String? = null

    init {
        mContext = context
        mMediaPlayer = MediaPlayer()
    }

    fun setPath(path: String) {
        mPath = path
        if (mMediaPlayer?.isPlaying!!) mMediaPlayer?.reset()
        mMediaPlayer?.setDataSource(mContext!!, Uri.parse(path))
        mMediaPlayer?.prepareAsync()
        mMediaPlayer?.setOnPreparedListener {
            mOnMediaPlayerHelperListener?.invoke(it)
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra ->
            LogUtils.log(LogUtils.E, "MediaPlayerHelper", "播放错误，错误代码是$what，和$extra")
            true
        }
    }

    fun getPath(): String? {
        return mPath
    }

    fun start() {
        if (mMediaPlayer?.isPlaying!!) return
        mMediaPlayer?.start()
    }

    fun pause() {
        if (mMediaPlayer?.isPlaying!!) mMediaPlayer?.pause()
    }

    fun destroy() {
        if (mMediaPlayer?.isPlaying!!) {
            mPath = null
            mMediaPlayer?.stop()
            mMediaPlayer?.reset()
        }
    }

    fun setOnMediaPlayerHelperListener(onMediaPlayerHelperListener: (mp: MediaPlayer) -> Unit) {
        mOnMediaPlayerHelperListener = onMediaPlayerHelperListener
    }

    interface OnMediaPlayerHelperListener {
        fun onPrepared(mp: MediaPlayer)
    }

}