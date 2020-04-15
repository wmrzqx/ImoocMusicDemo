package com.zqx.imoocmusicdemo.user

import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 4:24
 */
class UserCenterModel : UserCenterContract.IUserCenterModel {

    override fun requestUserInfo(
        phone: String?,
        onRequestUserSuccess: (UserBean) -> Unit,
        onRequestUserFailure: () -> Unit
    ) {
        val user =
            UserHelper.getInstance(MyApplication.applicationContext)
                ?.getUserFromRealmByPhone(phone!!)
        if (user != null) {
            onRequestUserSuccess.invoke(user)
        } else {
            onRequestUserFailure.invoke()
        }
    }

}