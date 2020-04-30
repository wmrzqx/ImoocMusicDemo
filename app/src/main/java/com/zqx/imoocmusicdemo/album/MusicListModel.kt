package com.zqx.imoocmusicdemo.album

import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.helper.DataSourceHelper
import com.zqx.imoocmusicdemo.helper.RealmHelper

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.album
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 14:21
 */
class MusicListModel : MusicListContract.IMusicListModel {
    override fun requestMusicList(
        albumId: String,
        onSuccess: (RealmHelper, MutableList<MusicBean>?) -> Unit
    ) {
        DataSourceHelper.getMusicListFromRealm(albumId, onSuccess)
    }
}