package com.zqx.imoocmusicdemo.login

import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.RealmHelper
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Administrator on 2020/04/11 22:46.
 */
class LoginModel : LoginContract.ILoginModel {
    override fun login(
        user: UserBean,
        onLoginSuccess: (UserBean) -> Unit,
        onLoginFailure: (cause: String) -> Unit
    ) {
        UserHelper.getInstance(MyApplication.applicationContext)
            ?.login(user, onLoginSuccess, onLoginFailure)
    }

}