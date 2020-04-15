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
open class MusicSourceBean : RealmObject() {
    var album: RealmList<AlbumBean>? = null
    var hot: RealmList<MusicBean>? = null
}