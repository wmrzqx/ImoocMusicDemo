package com.zqx.imoocmusicdemo.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by Administrator on 2020/04/14 2:09.
 */
open class UserBean : RealmObject() {
    @PrimaryKey
    var phone: String = ""

    @Required
    var password: String = ""
}