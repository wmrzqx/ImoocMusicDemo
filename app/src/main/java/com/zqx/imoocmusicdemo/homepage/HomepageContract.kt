package com.zqx.imoocmusicdemo.homepage

import com.zqx.imoocmusicdemo.bean.MusicSourceBean

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.homepage
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 10:04
 */
interface HomepageContract {
    interface IHomepageView {
        fun refreshUI(musicSource: MusicSourceBean)
        fun showToast(msg: String)
    }

    interface IHomepageModel {
        fun requestMusicSource(
            onSuccess: (MusicSourceBean) -> Unit,
            onFailure: (cause: String) -> Unit
        )
    }

    interface IHomepagePresenter {
        fun requestMusicSource()
    }
}