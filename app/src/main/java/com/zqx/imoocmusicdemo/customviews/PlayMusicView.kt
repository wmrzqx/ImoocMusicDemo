package com.zqx.imoocmusicdemo.customviews

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.music.*

/**
 * Created by Administrator on 2020/04/13 4:25.
 */
class PlayMusicView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    init {
        init(context)
    }

    private var mView: View? = null
    private var iv_play_music_icon: ImageView? = null

    private var mPlayMusicAnim: ObjectAnimator? = null
    private var mPlayNeedleAnim: ObjectAnimator? = null
    private var mStopNeedleAnim: ObjectAnimator? = null

    private var fl_play_music: FrameLayout? = null
    private var iv_play_music_needle: ImageView? = null
    private var iv_play_music: ImageView? = null

    private var currentNeedleValue: Float = -20f

    private var mMediaController: MediaControllerCompat? = null
    private var mControllerCallback: ControllerCallback? = null

    private fun init(context: Context) {
        mView = LayoutInflater.from(context).inflate(R.layout.play_music, this, false)
        iv_play_music_icon = mView?.findViewById(R.id.iv_play_music_icon)
        fl_play_music = mView?.findViewById(R.id.fl_play_music)
        iv_play_music_needle = mView?.findViewById(R.id.iv_play_music_needle)
        iv_play_music = mView?.findViewById(R.id.iv_play_music)

        mPlayMusicAnim = ObjectAnimator.ofFloat(fl_play_music, View.ROTATION, 0f, 360f)
        mPlayMusicAnim?.duration = resources.getInteger(R.integer.play_music_anim_duration).toLong()
        mPlayMusicAnim?.repeatCount = ObjectAnimator.INFINITE
        mPlayMusicAnim?.repeatMode = ObjectAnimator.RESTART
        mPlayMusicAnim?.interpolator = LinearInterpolator()

        iv_play_music_needle?.pivotX = 0F
        iv_play_music_needle?.pivotY = 0F
        mPlayNeedleAnim = ObjectAnimator.ofFloat(iv_play_music_needle, View.ROTATION, -20f, 0f)
        mPlayNeedleAnim?.duration =
            resources.getInteger(R.integer.play_needle_anim_duration).toLong()
        mPlayMusicAnim?.interpolator = LinearInterpolator()
        mPlayNeedleAnim?.addUpdateListener {
            currentNeedleValue = it.animatedValue as Float
        }

        mStopNeedleAnim = ObjectAnimator.ofFloat(iv_play_music_needle, View.ROTATION, 0f, -20f)
        mStopNeedleAnim?.duration =
            resources.getInteger(R.integer.play_needle_anim_duration).toLong()
        mStopNeedleAnim?.interpolator = LinearInterpolator()
        mStopNeedleAnim?.addUpdateListener {
            currentNeedleValue = it.animatedValue as Float
        }

        addView(mView)

    }

    fun setMediaController(mediaController: MediaControllerCompat?) {
        if (mediaController != null) {
            mControllerCallback = ControllerCallback()
            mediaController.registerCallback(mControllerCallback!!)
        } else if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mControllerCallback!!)
            mControllerCallback = null
        }
        mMediaController = mediaController
    }

    fun disconnectController() {
        mMediaController?.let {
            it.unregisterCallback(mControllerCallback!!)
            mControllerCallback = null
            mMediaController = null
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        fl_play_music?.setOnClickListener(l)
    }

    fun setMusicIcon(icon: String) {
        Glide.with(this).load(icon).circleCrop().into(iv_play_music_icon!!)
    }

    fun stopAnim() {
        iv_play_music?.visibility = View.VISIBLE
        if (!mPlayMusicAnim?.isPaused!!) {
            mPlayMusicAnim?.pause()
        }
        mPlayNeedleAnim?.cancel()
        mStopNeedleAnim?.setFloatValues(currentNeedleValue, -20f)
        mStopNeedleAnim?.start()
    }

    fun startAnim() {
        iv_play_music?.visibility = View.GONE
        if (!mPlayMusicAnim?.isStarted!!) {
            mPlayMusicAnim?.start()
        } else {
            mPlayMusicAnim?.resume()
        }
        mStopNeedleAnim?.cancel()
        mPlayNeedleAnim?.setFloatValues(currentNeedleValue, 0f)
        mPlayNeedleAnim?.start()
    }

    internal inner class ControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            when (state?.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    startAnim()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    stopAnim()
                }
                PlaybackStateCompat.STATE_STOPPED -> {
                    stopAnim()
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            mPlayMusicAnim?.cancel()
            startAnim()
        }
    }


}