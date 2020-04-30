package com.zqx.imoocmusicdemo.homepage

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.adapters.HomepageMusicAdapter
import com.zqx.imoocmusicdemo.adapters.HomepageAlbumAdapter
import com.zqx.imoocmusicdemo.album.KEY_ALBUM_ID
import com.zqx.imoocmusicdemo.album.MusicListActivity
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.bean.AlbumBean
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import com.zqx.imoocmusicdemo.customviews.GridDividerItemDecoration
import com.zqx.imoocmusicdemo.music.KEY_MUSIC_ID
import com.zqx.imoocmusicdemo.music.PlayMusicActivity
import com.zqx.imoocmusicdemo.user.UserCenterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    BaseMVPActivity<HomepageContract.IHomepageView, HomepageContract.IHomepagePresenter>(),
    HomepageContract.IHomepageView {

    private var homepageHeader: View? = null

    private var homepageAdapter: HomepageMusicAdapter? = null

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter?.requestMusicSource()
    }

    private fun initHeaderView(albums: MutableList<AlbumBean>) {
        homepageHeader =
            layoutInflater.inflate(R.layout.header_homepage_songlist, null, false)
        val rvHomepageRecommend: RecyclerView? =
            homepageHeader?.findViewById(R.id.rv_homepage_recommend)
        val homepageSongListAdapter = HomepageAlbumAdapter(this, albums)
        rvHomepageRecommend?.adapter = homepageSongListAdapter
        rvHomepageRecommend?.addItemDecoration(
            GridDividerItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.gridDividerWidth
                )
            )
        )
        val gridLayoutManager = GridLayoutManager(this, 3)
        rvHomepageRecommend?.layoutManager = gridLayoutManager
        homepageSongListAdapter.setOnItemClickListener { _, data ->
            ActivityUtils.startActivity(
                Intent(this, MusicListActivity::class.java).putExtra(
                    KEY_ALBUM_ID, data?.albumId
                )
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_mine -> {
                ActivityUtils.startActivity(this, UserCenterActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun createPresenter(): HomepageContract.IHomepagePresenter {
        return HomepagePresenter()
    }

    override fun refreshUI(musicSource: MusicSourceBean) {
        rv_homepage.layoutManager = LinearLayoutManager(this)
        initHeaderView(musicSource.album!!)
        homepageAdapter = HomepageMusicAdapter(this, musicSource.hot!!)
        rv_homepage.adapter = homepageAdapter
        homepageAdapter?.setHeader(homepageHeader)
        homepageAdapter?.setOnItemClickListener { position, data ->
            ActivityUtils.startActivity(
                Intent(
                    this,
                    PlayMusicActivity::class.java
                ).putExtra(KEY_MUSIC_ID, data?.musicId)
            )
        }
    }

    override fun showToast(msg: String) {
        ToastUtils.showShort(msg)
    }

}