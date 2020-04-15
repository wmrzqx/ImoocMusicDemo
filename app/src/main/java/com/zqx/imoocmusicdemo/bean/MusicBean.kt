package com.zqx.imoocmusicdemo.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2020/04/12 19:24.
 */
open class MusicBean : RealmObject() {
    var musicId: String? = null
    var name: String? = null
    var poster: String? = null
    var path: String? = null
    var author: String? = null
}