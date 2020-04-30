package com.zqx.imoocmusicdemo.helper

import com.blankj.utilcode.util.LogUtils
import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.AlbumBean
import com.zqx.imoocmusicdemo.bean.MusicBean
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

    fun saveDataSourceToRealm(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val realmHelper = RealmHelper()
        realmHelper.createModelFromJson(getDataSourceFromAssets(), onSuccess, onFailure)
        realmHelper.close()
    }

    fun deleteDataSourceFromRealm() {
        val realmHelper = RealmHelper()
        realmHelper.deleteOneModelAsync(MusicSourceBean::class.java, {
            LogUtils.i(DataSourceHelper::class.java.simpleName, "从Realm删除数据源成功")
        }, {
            it.printStackTrace()
            LogUtils.i(DataSourceHelper::class.java.simpleName, "从Realm删除数据源失败")
        })
        realmHelper.close()
    }

    fun getDataSourceFromRealm(
        onSuccess: (MusicSourceBean) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val realmHelper = RealmHelper()
        val result = realmHelper.getAllModel(MusicSourceBean::class.java)
        if (result == null || result.size <= 0) {
            onFailure("暂无数据")
            realmHelper.close()
            return
        }
        onSuccess(result[0])
//        realmHelper.close()
    }

    fun getMusicListFromRealm(
        albumId: String,
        onSuccess: (RealmHelper, MutableList<MusicBean>?) -> Unit
    ) {
        val realmHelper = RealmHelper()
        val result = realmHelper.getModelById(AlbumBean::class.java, "albumId", albumId)
        onSuccess(realmHelper, result?.list)
    }

    fun getMusicDetailFromRealm(musicId: String, onSuccess: (RealmHelper, MusicBean?) -> Unit) {
        val realmHelper = RealmHelper()
        onSuccess(realmHelper, realmHelper.getModelById(MusicBean::class.java, "musicId", musicId))
    }

}