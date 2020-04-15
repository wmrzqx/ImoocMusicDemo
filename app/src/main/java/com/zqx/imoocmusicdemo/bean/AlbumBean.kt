package com.zqx.imoocmusicdemo.bean

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2020/04/12 19:35.
 */
open class AlbumBean : RealmObject() {
    var albumId: String? = null
    var name: String? = null
    var poster: String? = null
    var playNum: String? = null
    var list: RealmList<MusicBean>? = null
}