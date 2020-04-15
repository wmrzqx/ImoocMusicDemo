package com.zqx.imoocmusicdemo.helper

import android.media.MediaPlayer
import android.net.Uri
import com.blankj.utilcode.util.LogUtils
import com.zqx.imoocmusicdemo.MyApplication

/**
 * Created by Administrator on 2020/04/13 6:45.
 */
object MediaPlayerHelper {

    private val mMediaPlayer: MediaPlayer = MediaPlayer()

    fun start(path: String, onPrepared: (MediaPlayer) -> Unit) {
        if (mMediaPlayer.isPlaying) mMediaPlayer.reset()
        mMediaPlayer.setDataSource(MyApplication.applicationContext, Uri.parse(path))
        mMediaPlayer.prepareAsync()
        mMediaPlayer.setOnPreparedListener(onPrepared)
        mMediaPlayer.setOnErrorListener { mp, what, extra ->
            LogUtils.log(LogUtils.E, "MediaPlayerHelper", "播放错误，错误代码是$what，和$extra")
            true
        }
    }

    fun resume() {
        if (!mMediaPlayer.isPlaying) mMediaPlayer.start()
    }

    fun pause() {
        if (mMediaPlayer.isPlaying) mMediaPlayer.pause()
    }

    fun destroy() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
        }
    }

}