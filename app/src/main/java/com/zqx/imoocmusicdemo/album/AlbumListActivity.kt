package com.zqx.imoocmusicdemo.album

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.adapters.HomepageMusicAdapter
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.music.PlayMusicActivity
import kotlinx.android.synthetic.main.activity_album_list.*

class AlbumListActivity : BaseActivity() {

    override fun initData(savedInstanceState: Bundle?) {
        val musicList = ArrayList<MusicBean>()
        (1..10).forEach {
            musicList.add(MusicBean().apply {
                musicName = "音乐$it"
            })
        }
        rv_album_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_album_list.layoutManager = LinearLayoutManager(this)
        val homepageAdapter = HomepageMusicAdapter(this, musicList)
        homepageAdapter.setHeader(
            layoutInflater.inflate(
                R.layout.headr_album_list,
                rv_album_list,
                false
            )
        )
        rv_album_list.adapter = homepageAdapter
        homepageAdapter.setOnItemClickListener { position, data ->
            ActivityUtils.startActivity(
                this,
                PlayMusicActivity::class.java
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_album_list
    }
}
