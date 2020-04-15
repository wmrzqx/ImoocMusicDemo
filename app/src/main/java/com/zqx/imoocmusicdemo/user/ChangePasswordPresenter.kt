package com.zqx.imoocmusicdemo.user

import com.blankj.utilcode.util.EncryptUtils
import com.zqx.imoocmusicdemo.base.mvp.BasePresenter

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.user
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/15
 * @time 7:02
 */
class ChangePasswordPresenter(private val model: ChangePasswordContract.IChangePasswordModel = ChangePasswordModel()) :
    BasePresenter<ChangePasswordContract.IChangePasswordView>(),
    ChangePasswordContract.IChangePasswordPresenter {

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        oldPassword.ifEmpty {
            mView?.showToast("请输入原密码")
            return
        }
        newPassword.ifEmpty {
            mView?.showToast("请输入新密码")
            return
        }
        confirmNewPassword.ifEmpty {
            mView?.showToast("请确认新密码")
            return
        }
        if (newPassword != confirmNewPassword) {
            mView?.showToast("两次密码输入不一致")
            return
        }

        model.changePassword(
            EncryptUtils.encryptMD5ToString(oldPassword),
            EncryptUtils.encryptMD5ToString(newPassword),
            {
                mView?.showToast("修改密码成功")
                mView?.closeActivity()
            },
            {
                mView?.showToast(it)
            })
    }

}