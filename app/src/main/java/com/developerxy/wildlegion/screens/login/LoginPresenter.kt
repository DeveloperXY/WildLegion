package com.developerxy.wildlegion.screens.login

import android.widget.Toast
import com.developerxy.wildlegion.data.UserRepository
import com.developerxy.wildlegion.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy


class LoginPresenter(var mView: LoginContract.View,
                     var mFirebaseAuth: FirebaseAuth,
                     var mFirebaseDatabase: FirebaseDatabase) : LoginContract.Presenter {

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
                        val currentUserId = mFirebaseAuth.currentUser?.uid!!
                        mFirebaseDatabase.reference
                                .child("Users")
                                .child(currentUserId)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(ds: DataSnapshot) {
                                        val email = ds.child("email").value.toString()
                                        val gamerangerId = ds.child("gamerangerId").value.toString()
                                        val nickname = ds.child("nickname").value.toString()

                                        val userRepository = UserRepository.getInstance(mView.getContext())
                                        userRepository.insert(User(nickname, gamerangerId, email))
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeBy(
                                                        onError = {
                                                            Toast.makeText(mView.getContext(), "Error: $it", Toast.LENGTH_LONG).show()
                                                        },
                                                        onComplete = {
                                                            mView.hideProgressBar()
                                                            if (mView.isLoginOngoing())
                                                                mView.finish(nickname)
                                                            else
                                                                mView.openMainActivity()
                                                        }
                                                )
                                    }
                                })
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