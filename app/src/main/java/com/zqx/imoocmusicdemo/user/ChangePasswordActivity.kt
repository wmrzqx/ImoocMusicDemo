package com.zqx.imoocmusicdemo.user

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import kotlinx.android.synthetic.main.activity_change_password.*

/**
 * @author 小zou
 * @date
 */
class ChangePasswordActivity :
    BaseMVPActivity<ChangePasswordContract.IChangePasswordView, ChangePasswordContract.IChangePasswordPresenter>(),
    ChangePasswordContract.IChangePasswordView {

    override fun initData(savedInstanceState: Bundle?) {
        btn_confirm_change.setOnClickListener {
            val oldPassword = et_old_password.text.toString()
            val newPassword = et_new_password.text.toString()
            val confirmNewPassword = et_confirm_new_password.text.toString()
            mPresenter?.changePassword(oldPassword, newPassword, confirmNewPassword)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_change_password
    }

    override fun createPresenter(): ChangePasswordContract.IChangePasswordPresenter {
        return ChangePasswordPresenter()
    }

    override fun showToast(toast: String) {
        ToastUtils.showShort(toast)
    }

    override fun closeActivity() {
        finish()
    }
}