package com.zqx.imoocmusicdemo.user

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 6:36
 */
interface ChangePasswordContract {
    interface IChangePasswordView {
        fun showToast(toast: String)
        fun closeActivity()
    }

    interface IChangePasswordModel {
        fun changePassword(
            oldPassword: String,
            newPassword: String,
            onChangePasswordSuccess: () -> Unit,
            onChangePasswordFailure: (cause: String) -> Unit
        )
    }

    interface IChangePasswordPresenter {
        fun changePassword(oldPassword: String, newPassword: String, confirmNewPassword: String)
    }

}