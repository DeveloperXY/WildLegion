package com.developerxy.wildlegion.screens.login

import com.developerxy.wildlegion.BasePresenter

interface LoginContract {
    interface View {
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
    }

    interface Presenter : BasePresenter {
        fun logUserIn(email: String, password: String)
        fun togglePasswordVisibility()
    }
}