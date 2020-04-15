package com.zqx.imoocmusicdemo.register

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.base.mvp.BasePresenter
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.UserHelper

/**
 * Created by Administrator on 2020/04/14 2:41.
 */
class RegisterPresenter(private val model: RegisterModel = RegisterModel()) :
    RegisterContract.IRegisterPresenter,
    BasePresenter<RegisterContract.IRegisterView>() {

    override fun registerUser(phone: String, password: String, passwordConfirm: String) {
        if (!RegexUtils.isMobileExact(phone)) {
            mView?.showToast("请输入正确的手机号")
            return
        }
        password.ifEmpty {
            mView?.showToast("请输入密码")
            return
        }
        passwordConfirm.ifEmpty {
            mView?.showToast("请确认密码")
            return
        }
        if (!StringUtils.equals(password, passwordConfirm)) {
            mView?.showToast("两次密码输入不一致")
            return
        }
        model.registerUser(UserBean().apply {
            this.phone = phone
            this.password = EncryptUtils.encryptMD5ToString(password)
        }, {
            UserHelper.getInstance(MyApplication.applicationContext)?.saveUserBySp(it)
            mView?.showToast("注册成功")
            mView?.intoHomepage()
        }, {
            mView?.showToast(it)
        })
    }

}