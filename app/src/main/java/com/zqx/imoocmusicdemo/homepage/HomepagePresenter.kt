package com.zqx.imoocmusicdemo.homepage

import com.zqx.imoocmusicdemo.base.mvp.BasePresenter

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.homepage
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/20
 * @time 10:11
 */
class HomepagePresenter(private val model: HomepageContract.IHomepageModel = HomepageModel()) :
    BasePresenter<HomepageContract.IHomepageView>(),
    HomepageContract.IHomepagePresenter {
    override fun requestMusicSource() {
        model.requestMusicSource({
            mView?.refreshUI(it)
        }, {
            mView?.showToast(it)
        })
    }
}