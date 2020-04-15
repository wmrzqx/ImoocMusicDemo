package com.zqx.imoocmusicdemo.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.BaseActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_play_music.*

class PlayMusicActivity : BaseActivity() {

    override fun initData(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        iv_play_music_back.setOnClickListener { onBackPressed() }
        Glide.with(this)
            .load("https://i0.hdslb.com/bfs/article/c1e19e327e75dbcc05d878acf113d9a87ddfafb2.jpg")
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 10)))
            .into(iv_play_music_bg)
        playMusicView.setMusicIcon("https://i0.hdslb.com/bfs/article/c1e19e327e75dbcc05d878acf113d9a87ddfafb2.jpg")
        playMusicView.playMusic("http://m801.music.126.net/20200414012714/74d59d19c3ce4b9ac98d2c101d025e2e/jdymusic/obj/w5zDlMODwrDDiGjCn8Ky/1916818269/c98d/2e26/4c1a/2f3768f5a0b9ac06a39ecd697d142f47.mp3")
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_play_music
    }

    override fun isNeedActionBar(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        playMusicView.destroyMediaPlayer()
    }
}
