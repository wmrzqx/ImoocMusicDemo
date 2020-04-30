package com.zqx.imoocmusicdemo.album

import com.zqx.imoocmusicdemo.base.mvp.BasePresenter

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.album
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 14:22
 */
class MusicListPresenter(private val model: MusicListContract.IMusicListModel = MusicListModel()) :
    BasePresenter<MusicListContract.IMusicListView>(),
    MusicListContract.IMusicListPresenter {
    override fun requestMusicList(albumId: String) {
        model.requestMusicList(albumId) { realmHelper, mutableList ->
            mView?.refreshUI(realmHelper, mutableList)
        }
    }
}