package com.developerxy.wildlegion.screens.login

import com.developerxy.wildlegion.BasePresenter
import com.developerxy.wildlegion.BaseView

interface LoginContract {
    interface View : BaseView {
        fun displayWlLogo()
        fun displayShcBackground()
        fun setListenerOnLoginButton()
        fun hideProgressBar()
        fun showProgressBar()
        fun disableLoginButton()
        fun enableLoginButton()
        fun setLoginButtonText(text: String?)
        fun showLoginError()
        fun openMainActivity()
        fun setListenerOnVisibilitySwitch()
        fun showPassword()
        fun hidePassword()
        fun showLoginButton()
        fun hideLoginButton()
        fun isLoginOngoing(): Boolean
        fun finish(nickname: String)
    }

    interface Presenter : BasePresenter {
        fun logUserIn(email: String, password: String)
        fun togglePasswordVisibility()
    }
}