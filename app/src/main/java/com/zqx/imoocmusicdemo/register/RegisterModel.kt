package com.zqx.imoocmusicdemo.register

import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.RealmHelper
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Administrator on 2020/04/14 4:37.
 */
class RegisterModel : RegisterContract.IRegisterModel {

    override fun registerUser(
        user: UserBean,
        onRegisterSuccess: (UserBean) -> Unit,
        onRegisterFailure: (cause: String) -> Unit
    ) {
        UserHelper.getInstance(MyApplication.applicationContext)
            ?.registerUser(user, onRegisterSuccess, onRegisterFailure)
    }

}