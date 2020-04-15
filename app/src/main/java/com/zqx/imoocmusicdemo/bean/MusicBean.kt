package com.zqx.imoocmusicdemo.bean

import io.realm.RealmObject

/**
 * Created by Administrator on 2020/04/12 19:24.
 */
class MusicBean : RealmObject() {
    lateinit var musicId: String
    lateinit var name: String
    lateinit var poster: String
    lateinit var path: String
    lateinit var author: String
}