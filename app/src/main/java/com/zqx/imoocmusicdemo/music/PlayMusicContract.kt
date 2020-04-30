package com.zqx.imoocmusicdemo.music

import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.helper.RealmHelper

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.music
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 15:02
 */
interface PlayMusicContract {
    interface IPlayMusicView {
        fun refreshUI(musicBean: MusicBean?)
    }

    interface IPlayMusicModel {
        fun getMusicDetail(musicId: String, onSuccess: (RealmHelper, MusicBean?) -> Unit)
    }

    interface IPlayMusicPresenter {
        fun getMusicDetail(musicId: String)
    }
}