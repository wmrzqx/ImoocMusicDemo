package com.zqx.imoocmusicdemo.music

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import com.zqx.imoocmusicdemo.helper.RealmHelper


/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.music
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/27
 * @time 21:23
 */
object MusicLibrary {

    private val musicPosterBitmap = HashMap<String, Bitmap>()

    fun getMediaItems(
        context: Context,
        onGetMediaItemsSuccess: (MutableList<MediaBrowserCompat.MediaItem>) -> Unit
    ) {
        val result = ArrayList<MediaBrowserCompat.MediaItem>()
        val realmHelper = RealmHelper()
        val musicSourceBean = realmHelper.getAllModel(MusicSourceBean::class.java)
        musicSourceBean?.get(0)?.hot?.forEach {
            Glide.with(context).asBitmap().load(it.poster).diskCacheStrategy(
                DiskCacheStrategy.RESOURCE
            ).into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    musicPosterBitmap[it.musicId!!] = resource
                }
            })
            result.add(
                MediaBrowserCompat.MediaItem(
                    MediaDescriptionCompat.Builder().run {
                        setMediaId(it.musicId)
                        setTitle(it.name)
                        setSubtitle(it.author)
                        setMediaUri(Uri.parse(it.path))
                        setIconUri(Uri.parse(it.poster))
                        build()
                    },
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                )
            )
        }
        onGetMediaItemsSuccess(result)
        realmHelper.close()
    }

    fun getMusicPosterById(musicId: String): Bitmap? {
        return musicPosterBitmap[musicId]
    }

    fun clearMusicPosterBitmap() {
        musicPosterBitmap.clear()
    }

    fun getMetadata(musicId: String): MediaMetadataCompat {
        val realmHelper = RealmHelper()
        val mediaMetadata = realmHelper.run {
            getModelById(MusicBean::class.java, "musicId", musicId)?.let {
                return@run MediaMetadataCompat.Builder().apply {
                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it.musicId)
                    putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, it.author)
                    putString(MediaMetadataCompat.METADATA_KEY_ART_URI, it.poster)
                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, it.path)
                    putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.name)
                    putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.duration!!)
                }.build()
            }
        }
        realmHelper.close()
        return mediaMetadata!!
    }

    fun getRoot(): String {
        return "MusicRoot"
    }

}