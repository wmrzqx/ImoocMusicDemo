package com.zqx.imoocmusicdemo.homepage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.adapters.HomepageMusicAdapter
import com.zqx.imoocmusicdemo.adapters.HomepageAlbumAdapter
import com.zqx.imoocmusicdemo.album.AlbumListActivity
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.bean.AlbumBean
import com.zqx.imoocmusicdemo.customviews.GridDividerItemDecoration
import com.zqx.imoocmusicdemo.music.PlayMusicActivity
import com.zqx.imoocmusicdemo.user.UserCenterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private var homepageHeader: View? = null

    private var homepageAdapter: HomepageMusicAdapter? = null


    override fun initData(savedInstanceState: Bundle?) {
        rv_homepage.layoutManager = LinearLayoutManager(this)
        initHeaderView()
        val homepageMusicList = ArrayList<MusicBean>()
        (1..6).forEach {
            val musicBean = MusicBean()
            musicBean.name = "最新音乐$it"
            homepageMusicList.add(musicBean)
        }
        homepageAdapter = HomepageMusicAdapter(this, homepageMusicList)
        rv_homepage.adapter = homepageAdapter
        homepageAdapter?.setHeader(homepageHeader)
        homepageAdapter?.setOnItemClickListener { position, data ->
            ActivityUtils.startActivity(
                this,
                PlayMusicActivity::class.java
            )
        }
    }

    private fun initHeaderView() {
        homepageHeader =
            layoutInflater.inflate(R.layout.header_homepage_songlist, null, false)
        val rvHomepageRecommend: RecyclerView? =
            homepageHeader?.findViewById(R.id.rv_homepage_recommend)
        val homepageSongList = ArrayList<AlbumBean>()
        for (i in 1..6) {
            val homepageSongListBean =
                AlbumBean()
            homepageSongListBean.name = "推荐歌单$i"
            homepageSongListBean.playNum = (i * 100 * 10000).toString()
            homepageSongList.add(homepageSongListBean)
        }
        val homepageSongListAdapter = HomepageAlbumAdapter(this, homepageSongList)
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
        homepageSongListAdapter.setOnItemClickListener { position, data ->
            ActivityUtils.startActivity(this, AlbumListActivity::class.java)
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

}