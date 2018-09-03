package com.developerxy.wildlegion.screens.login

import com.google.firebase.auth.FirebaseAuth


class LoginPresenter(var mView: LoginContract.View,
                     var mFirebaseAuth: FirebaseAuth) : LoginContract.Presenter {

    var isPasswordRevealed = false

    override fun start() {
        mView.displayWlLogo()
        mView.displayShcBackground()
        mView.setListenerOnLoginButton()
        mView.setListenerOnVisibilitySwitch()
    }

    override fun logUserIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty())
            return

        mView.hideLoginButton()
        mView.showProgressBar()
        mView.setLoginButtonText("Signing you in...")
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        mView.hideProgressBar()
                        mView.openMainActivity()
                    } else {
                        mView.hideProgressBar()
                        mView.showLoginButton()
                        mView.setLoginButtonText("Sign in")
                        mView.showLoginError()
                    }
                }
    }

    override fun togglePasswordVisibility() {
        isPasswordRevealed = !isPasswordRevealed
        if (isPasswordRevealed)
            mView.showPassword()
        else
            mView.hidePassword()
    }
}