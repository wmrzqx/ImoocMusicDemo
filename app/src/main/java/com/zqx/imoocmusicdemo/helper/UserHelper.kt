package com.zqx.imoocmusicdemo.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.GsonUtils
import com.zqx.imoocmusicdemo.MyApplication
import com.zqx.imoocmusicdemo.bean.UserBean

/**
 * Created by Administrator on 2020/04/14 7:59.
 */
class UserHelper private constructor(
    private val mContext: Context,
    private val sp: SharedPreferences = mContext.getSharedPreferences(
        "sp_user_data",
        Context.MODE_PRIVATE
    )
) {

    companion object {
        private var mInstance: UserHelper? = null
        fun getInstance(mContext: Context): UserHelper? {
            if (mInstance == null) {
                synchronized(UserHelper::class.java) {
                    if (mInstance == null) {
                        mInstance = UserHelper(mContext.applicationContext)
                    }
                }
            }
            return mInstance
        }

        private const val KEY_USER_DATA = "key_user_data"

    }

    fun saveUserBySp(user: UserBean) {
        sp.edit(false) { putString(KEY_USER_DATA, GsonUtils.toJson(user, UserBean::class.java)) }
//        val editor = sp.edit()
//        editor.putString(KEY_USER_DATA, GsonUtils.toJson(user, UserBean::class.java))
//        editor.apply()
    }

    fun getUserBySp(): UserBean? {
        val userJson = sp.getString(KEY_USER_DATA, null)
        return when {
            userJson.isNullOrBlank() -> null
            else -> GsonUtils.fromJson(userJson, UserBean::class.java)
        }
    }

    fun clearLocalUserBySp() {
        sp.edit(false) { clear() }
//        sp.edit().clear().apply()
    }

    fun isUserLogged(): Boolean {
        val userBySp = getUserBySp()
        return userBySp?.phone != null
    }

    fun getUserFromRealmByPhone(phone: String): UserBean? {
        val mRealmHelper = RealmHelper()
        val user = mRealmHelper.getFirstModelByPrimaryKey(
            UserBean::class.java,
            "phone",
            phone
        )
        val userBean = UserBean().apply {
            this.phone = user?.phone.toString()
        }
        mRealmHelper.close()
        return userBean
    }

    fun registerUser(
        user: UserBean, onRegisterSuccess: (UserBean) -> Unit,
        onRegisterFailure: (cause: String) -> Unit
    ) {
        val mRealmHelper = RealmHelper()
        val userBean = getUserFromRealmByPhone(user.phone)
        when {
            userBean?.phone != user.phone -> mRealmHelper.saveModelAsync(user, {
                onRegisterSuccess.invoke(UserBean().apply {
                    this.phone = user.phone
                })
            }, {
                it.printStackTrace()
                onRegisterFailure.invoke("注册失败")
            })
            else -> onRegisterFailure.invoke("该手机号已经被注册")
        }
        mRealmHelper.close()
    }

    fun login(
        user: UserBean,
        onLoginSuccess: (UserBean) -> Unit,
        onLoginFailure: (cause: String) -> Unit
    ) {
        val mRealmHelper = RealmHelper()
        val userBean =
            mRealmHelper.getFirstModelByPrimaryKey(
                UserBean::class.java,
                "phone",
                user.phone
            )
        if (userBean == null) {
            onLoginFailure.invoke("用户名不存在")
            mRealmHelper.close()
            return
        }
        if (user.password != userBean.password) {
            onLoginFailure.invoke("密码错误")
            mRealmHelper.close()
            return
        }
        onLoginSuccess.invoke(UserBean().apply {
            this.phone = userBean.phone
        })
        mRealmHelper.close()
    }

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onChangePasswordSuccess: () -> Unit,
        onChangePasswordFailure: (cause: String) -> Unit
    ) {
        val mRealmHelper = RealmHelper()

        val user = mRealmHelper.getFirstModelByPrimaryKey(
            UserBean::class.java,
            "phone",
            getUserBySp()?.phone!!
        )
        if (user?.password != oldPassword) {
            onChangePasswordFailure.invoke("原密码错误，修改密码失败")
            return
        }
        if (user.password == newPassword) {
            onChangePasswordFailure.invoke("修改密码失败，新密码不能跟原密码相同")
            return
        }
        mRealmHelper.updateOneModelAsync(UserBean().apply {
            this.phone = getUserBySp()?.phone!!
            this.password = newPassword
        }, onChangePasswordSuccess, {
            it.printStackTrace()
            onChangePasswordFailure.invoke("系统错误，请稍后再试")
        })
        mRealmHelper.close()
    }

}