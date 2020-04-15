package com.zqx.imoocmusicdemo

import android.app.Application
import com.blankj.utilcode.util.Utils
import io.realm.Realm

/**
 * Created by Administrator on 2020/04/10 4:20.
 */
class MyApplication : Application() {

    companion object {
        lateinit var applicationContext: Application
    }

    override fun onCreate() {
        super.onCreate()
        MyApplication.applicationContext = this
        Utils.init(this)
        Realm.init(this)
    }

}