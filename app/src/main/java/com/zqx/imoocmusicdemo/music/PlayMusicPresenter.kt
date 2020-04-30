package com.zqx.imoocmusicdemo.music

import com.zqx.imoocmusicdemo.base.mvp.BasePresenter

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.music
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 15:07
 */
class PlayMusicPresenter(private val model: PlayMusicContract.IPlayMusicModel = PlayMusicModel()) :
    BasePresenter<PlayMusicContract.IPlayMusicView>(),
    PlayMusicContract.IPlayMusicPresenter {
    override fun getMusicDetail(musicId: String) {
        model.getMusicDetail(musicId) { realmHelper, musicBean ->
            mView?.refreshUI(musicBean)
            realmHelper.close()
        }
    }
}