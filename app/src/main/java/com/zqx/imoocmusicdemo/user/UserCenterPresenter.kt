package com.zqx.imoocmusicdemo.user

import com.blankj.utilcode.util.LogUtils
import com.zqx.imoocmusicdemo.base.mvp.BasePresenter

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 5:05
 */
class UserCenterPresenter(private val model: UserCenterModel = UserCenterModel()) :
    BasePresenter<UserCenterContract.IUserCenterView>(),
    UserCenterContract.IUserCenterPresenter {

    override fun requestUserInfo(phone: String?) {
        model.requestUserInfo(phone, {
            mView?.refreshUI(it)
        }, {
            LogUtils.e("查询用户信息失败")
        })
    }

}