package com.zqx.imoocmusicdemo.homepage

import com.zqx.imoocmusicdemo.bean.AlbumBean
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import com.zqx.imoocmusicdemo.helper.DataSourceHelper
import io.realm.RealmList

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.homepage
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 10:15
 */
class HomepageModel : HomepageContract.IHomepageModel {
    override fun requestMusicSource(
        onSuccess: (MusicSourceBean) -> Unit,
        onFailure: (cause: String) -> Unit
    ) {
        DataSourceHelper.getDataSourceFromRealm(onSuccess, onFailure)
    }

}