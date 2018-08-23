package com.developerxy.wildlegion.screens.login

import com.google.firebase.auth.FirebaseAuth


class LoginPresenter(var mView: LoginContract.View) : LoginContract.Presenter {

    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
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

        mView.showProgressBar()
        mView.disableLoginButton()
        mView.setLoginButtonText("Signing you in...")
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener({
                    if (it.isSuccessful) {
                        mView.openMainActivity()
                    } else {
                        mView.hideProgressBar()
                        mView.enableLoginButton()
                        mView.setLoginButtonText("Sign in")
                        mView.showLoginError()
                    }
                })
    }

    override fun togglePasswordVisibility() {
        isPasswordRevealed = !isPasswordRevealed
        if (isPasswordRevealed)
            mView.showPassword()
        else
            mView.hidePassword()
    }
}