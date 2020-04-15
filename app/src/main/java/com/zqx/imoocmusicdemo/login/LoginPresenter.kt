package com.zqx.imoocmusicdemo.login

import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.zqx.imoocmusicdemo.base.mvp.BasePresenter
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.DataSourceHelper
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Administrator on 2020/04/11 21:57.
 */
class LoginPresenter(private val loginModel: LoginContract.ILoginModel = LoginModel()) :
    BasePresenter<LoginContract.ILoginView>(), LoginContract.ILoginPresenter {

    override fun login(mobileNumber: String, password: String) {
        if (!RegexUtils.isMobileExact(mobileNumber)) {
            mView?.showMsg("无效手机号")
            return
        }
        password.ifEmpty {
            mView?.showMsg("请输入密码")
            return
        }
        loginModel.login(UserBean().apply {
            this.phone = mobileNumber
            this.password = EncryptUtils.encryptMD5ToString(password)
        }, {
            UserHelper.getInstance(
                when (mView) {
                    is Fragment -> {
                        (mView as Fragment).activity
                    }
                    else -> {
                        (mView as Context).applicationContext
                    }
                }!!
            )?.saveUserBySp(it)
            DataSourceHelper.saveDataSourceToRealm()
            mView?.intoHomepage()
        }, {
            mView?.showMsg(it)
        })
    }

}