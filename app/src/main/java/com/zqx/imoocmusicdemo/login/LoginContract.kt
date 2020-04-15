package com.zqx.imoocmusicdemo.login

import com.zqx.imoocmusicdemo.bean.UserBean

/**
 * Created by Administrator on 2020/04/11 21:59.
 */
interface LoginContract {

    interface ILoginView {
        fun showMsg(msg: String)
        fun intoHomepage()
    }

    interface ILoginModel {
        fun login(
            user: UserBean,
            onLoginSuccess: (UserBean) -> Unit,
            onLoginFailure: (cause: String) -> Unit
        )
    }

    interface ILoginPresenter {
        fun login(mobileNumber: String, password: String)
    }

}