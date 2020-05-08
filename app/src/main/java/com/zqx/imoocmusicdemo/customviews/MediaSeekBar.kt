package com.zqx.imoocmusicdemo.customviews

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import com.zqx.imoocmusicdemo.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * SeekBar that can be used with a [MediaSessionCompat] to track and seek in playing
 * media.
 */
class MediaSeekBar : FrameLayout {

    private var mMediaController: MediaControllerCompat? = null
    private var mControllerCallback: ControllerCallback? = null
    private var mIsTracking = false

    private var tvCurrentProgress: TextView? = null
    private var tvTotalProgress: TextView? = null
    private var mediaSeekBar: AppCompatSeekBar? = null

    private val mOnSeekBarChangeListener: SeekBar.OnSeekBarChangeListener =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                tvCurrentProgress?.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(progress.toLong()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mIsTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mMediaController!!.transportControls.seekTo(seekBar?.progress?.toLong()!!)
                mIsTracking = false
            }
        }

    private var mProgressAnimator: ValueAnimator? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        val seekBarRoot = LayoutInflater.from(context).inflate(R.layout.media_seekbar, null, false)
        tvCurrentProgress = seekBarRoot.findViewById(R.id.tv_current_progress)
        tvTotalProgress = seekBarRoot.findViewById(R.id.tv_total_progress)
        mediaSeekBar = seekBarRoot.findViewById(R.id.mediaSeekBar)
        mediaSeekBar?.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
        addView(seekBarRoot)
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
        if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mControllerCallback!!)
            mControllerCallback = null
            mMediaController = null
        }
    }

    private inner class ControllerCallback : MediaControllerCompat.Callback(),
        AnimatorUpdateListener {
        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            super.onPlaybackStateChanged(state)

            // If there's an ongoing animation, stop it now.
            if (mProgressAnimator != null) {
                mProgressAnimator!!.cancel()
                mProgressAnimator = null
            }
            val progress = state.position.toInt()
            mediaSeekBar?.progress = progress

            var max = mediaSeekBar?.max ?: 0

            if (max <= 100) {
                max = mMediaController!!.metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                    .toInt()
            }

            // If the media is playing then the seekbar should follow it, and the easiest
            // way to do that is to create a ValueAnimator to update it so the bar reaches
            // the end of the media the same time as playback gets there (or close enough).
            if (state.state == PlaybackStateCompat.STATE_PLAYING) {
                val timeToEnd = ((max - progress) / state.playbackSpeed).toInt()
                mProgressAnimator = ValueAnimator.ofInt(progress, max).apply {
                    duration = timeToEnd.toLong()
                    interpolator = LinearInterpolator()
                    addUpdateListener(this@ControllerCallback)
                    start()
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            super.onMetadataChanged(metadata)
            val max = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
//            progress = 0
            mediaSeekBar?.max = max.toInt()

            tvTotalProgress?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(max))

        }


        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            // If the user is changing the slider, cancel the animation.
            if (mIsTracking) {
                valueAnimator.cancel()
                return
            }
            val animatedIntValue = valueAnimator.animatedValue as Int
            mediaSeekBar?.progress = animatedIntValue
        }
    }

}