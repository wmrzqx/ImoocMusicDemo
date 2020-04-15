package com.zqx.imoocmusicdemo.register

import com.zqx.imoocmusicdemo.bean.UserBean

/**
 * Created by Administrator on 2020/04/14 2:30.
 */
class RegisterContract {
    interface IRegisterModel {
        fun registerUser(
            user: UserBean,
            onRegisterSuccess: (UserBean) -> Unit,
            onRegisterFailure: (cause: String) -> Unit
        )
    }

    interface IRegisterView {
        fun showToast(toast: String)
        fun intoHomepage()
    }

    interface IRegisterPresenter {
        fun registerUser(phone: String, password: String, passwordConfirm: String)
    }

}