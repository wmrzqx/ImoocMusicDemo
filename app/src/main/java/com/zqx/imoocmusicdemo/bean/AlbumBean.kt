package com.zqx.imoocmusicdemo.bean

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Administrator on 2020/04/12 19:35.
 */
class AlbumBean : RealmObject() {
    lateinit var albumId: String
    lateinit var name: String
    lateinit var poster: String
    lateinit var playNum: String
    lateinit var list: RealmList<MusicBean>
}