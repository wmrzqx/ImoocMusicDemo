package com.zqx.imoocmusicdemo.user

import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 6:59
 */
class ChangePasswordModel : ChangePasswordContract.IChangePasswordModel {
    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        onChangePasswordSuccess: () -> Unit,
        onChangePasswordFailure: (cause: String) -> Unit
    ) {
        UserHelper.getInstance(MyApplication.applicationContext)
            ?.changePassword(
                oldPassword,
                newPassword,
                onChangePasswordSuccess,
                onChangePasswordFailure
            )

    }
}