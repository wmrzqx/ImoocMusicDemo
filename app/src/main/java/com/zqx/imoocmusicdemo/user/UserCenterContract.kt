package com.zqx.imoocmusicdemo.user

import com.zqx.imoocmusicdemo.bean.UserBean

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 4:17
 */
interface UserCenterContract {
    interface IUserCenterView {
        fun refreshUI(user: UserBean)
    }

    interface IUserCenterModel {
        fun requestUserInfo(
            phone: String?,
            onRequestUserSuccess: (UserBean) -> Unit,
            onRequestUserFailure: () -> Unit
        )
    }

    interface IUserCenterPresenter {
        fun requestUserInfo(phone: String?)
    }

}