package com.zqx.imoocmusicdemo.helper

import com.blankj.utilcode.util.LogUtils
import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import java.io.InputStream

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.helper
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/16
 * @time 12:52
 */
object DataSourceHelper {
    private fun getDataSourceFromAssets(): InputStream {
        val assets = MyApplication.applicationContext.assets
        return assets.open("DataSource.json")
    }

    fun saveDataSourceToRealm() {
        val realmHelper = RealmHelper()
        realmHelper.createModelFromJson(getDataSourceFromAssets())
        realmHelper.close()
    }

    fun deleteDataSourceFromRealm() {
        val realmHelper = RealmHelper()
        realmHelper.deleteOneModelAsync(MusicSourceBean::class.java, {
            LogUtils.iTag(DataSourceHelper::class.java.simpleName, "从Realm删除数据源成功")
        }, {
            it.printStackTrace()
            LogUtils.iTag(DataSourceHelper::class.java.simpleName, "从Realm删除数据源失败")
        })
    }

}