package com.zqx.imoocmusicdemo.album

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.adapters.HomepageMusicAdapter
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.helper.RealmHelper
import com.zqx.imoocmusicdemo.music.KEY_MUSIC_ID
import com.zqx.imoocmusicdemo.music.PlayMusicActivity
import kotlinx.android.synthetic.main.activity_music_list.*

const val KEY_ALBUM_ID = "albumId"

class MusicListActivity :
    BaseMVPActivity<MusicListContract.IMusicListView, MusicListContract.IMusicListPresenter>(),
    MusicListContract.IMusicListView {

    private lateinit var realmHelper: RealmHelper

    private val homepageAdapter = HomepageMusicAdapter(this)

    override fun initData(savedInstanceState: Bundle?) {
        val albumId = intent.getStringExtra(KEY_ALBUM_ID)
        mPresenter?.requestMusicList(albumId!!)
        rv_album_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_album_list.layoutManager = LinearLayoutManager(this)

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
                Intent(
                    this,
                    PlayMusicActivity::class.java
                ).putExtra(KEY_MUSIC_ID, data?.musicId)
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_list
    }

    override fun createPresenter(): MusicListContract.IMusicListPresenter {
        return MusicListPresenter()
    }

    override fun refreshUI(realmHelper: RealmHelper, musics: MutableList<MusicBean>?) {
        this.realmHelper = realmHelper
        if (musics != null) {
            homepageAdapter.addAllData(musics)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmHelper.close()
    }

}
