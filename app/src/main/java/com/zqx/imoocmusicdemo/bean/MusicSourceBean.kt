package com.zqx.imoocmusicdemo.bean

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.bean
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 8:22
 */
class MusicSourceBean : RealmObject() {
    lateinit var album: RealmList<AlbumBean>
    lateinit var hot: RealmList<MusicBean>
}