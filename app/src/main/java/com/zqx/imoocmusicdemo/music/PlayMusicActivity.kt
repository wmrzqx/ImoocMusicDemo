package com.zqx.imoocmusicdemo.music

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.helper.MediaBrowserHelper
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_play_music.*

const val KEY_MUSIC_ID = "musicId"
const val ACTION_CLEAR_QUEUE = "action_clear_queue"

class PlayMusicActivity :
    BaseMVPActivity<PlayMusicContract.IPlayMusicView, PlayMusicContract.IPlayMusicPresenter>(),
    PlayMusicContract.IPlayMusicView {

    private lateinit var mMediaBrowserHelper: MediaBrowserHelper

    private var mIsPlaying = false

    private lateinit var currentMusic: MusicBean

    override fun initData(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mMediaBrowserHelper = MediaBrowserConnection(this)
        mMediaBrowserHelper.registerCallback(MediaBrowserListener())

        mPresenter?.getMusicDetail(intent.getStringExtra(KEY_MUSIC_ID)!!)

        iv_play_music_back.setOnClickListener { onBackPressed() }

    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMediaBrowserHelper.onStop()
        playMusicView.disconnectController()
        musicSeekBar.disconnectController()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_play_music
    }

    override fun isNeedActionBar(): Boolean {
        return false
    }

    override fun createPresenter(): PlayMusicContract.IPlayMusicPresenter {
        return PlayMusicPresenter()
    }

    override fun refreshUI(musicBean: MusicBean?) {
        currentMusic = musicBean!!
        Glide.with(this)
            .load(currentMusic.poster)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 10)))
            .into(iv_play_music_bg)
        tv_play_music_name.text = currentMusic.name
        tv_play_music_author.text = currentMusic.author
        playMusicView.setMusicIcon(currentMusic.poster!!)
        playMusicView.setOnClickListener {
            if (mIsPlaying) {
                mMediaBrowserHelper.transportControls.pause()
            } else {
                playMusicView.startAnim()
                mMediaBrowserHelper.transportControls.play()
            }
        }
    }

    internal inner class MediaBrowserConnection(context: Context) :
        MediaBrowserHelper(context, PlayMusicService2::class.java) {

        override fun onConnected(mediaController: MediaControllerCompat) {
            playMusicView.setMediaController(mediaController)
            musicSeekBar.setMediaController(mediaController)
        }

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            val mediaController = mediaController
            mediaController.transportControls.sendCustomAction(ACTION_CLEAR_QUEUE, null)
            children.forEach {
                mediaController.addQueueItem(it.description)
                if (it.description.mediaId == currentMusic.musicId) {
                    mMediaBrowserHelper.transportControls.skipToQueueItem(
                        children.indexOf(it).toLong()
                    )
                }
            }
        }

    }

    internal inner class MediaBrowserListener : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            mIsPlaying = state != null && state.state == PlaybackStateCompat.STATE_PLAYING

        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            metadata?.let {
                refreshUI(MusicBean().apply {
                    this.name = it.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                    this.author = it.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)
                    this.musicId = it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                    this.poster = it.getString(MediaMetadataCompat.METADATA_KEY_ART_URI)
                    this.path = it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
                })
            }
        }

    }

}
