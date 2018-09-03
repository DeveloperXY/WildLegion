package com.developerxy.wildlegion.screens.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginContract.View {

    lateinit var mPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mPresenter = LoginPresenter(this, FirebaseAuth.getInstance())
        mPresenter.start()
    }

    override fun displayWlLogo() {
        Glide.with(this)
                .load(R.drawable.wild_legion_full)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(wildLegionLogo)
    }

    override fun displayShcBackground() {
        Glide.with(this)
                .load(R.drawable.shc_background1)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(shcBackground)
    }

    override fun setListenerOnLoginButton() {
        loginButton.setOnClickListener {
            mPresenter.logUserIn(
                    email = emailField.text.toString(),
                    password = passwordField.text.toString())
        }
    }

    override fun hideProgressBar() {
        progressBar.visibility = GONE
    }

    override fun showProgressBar() {
        progressBar.visibility = VISIBLE
    }

    override fun disableLoginButton() {
        loginButton.isEnabled = false
    }

    override fun enableLoginButton() {
        loginButton.isEnabled = true
    }

    override fun showLoginButton() {
        loginButton.visibility = VISIBLE
    }

    override fun hideLoginButton() {
        loginButton.visibility = GONE
    }

    override fun setLoginButtonText(text: String?) {
        text ?: return
        loginButton.text = text
    }

    override fun showLoginError() {
        Toast.makeText(this, "Invalid email/password.", Toast.LENGTH_LONG).show()
    }

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun setListenerOnVisibilitySwitch() {
        visibilitySwitch.setOnClickListener {
            mPresenter.togglePasswordVisibility()
        }
    }

    override fun showPassword() {
        passwordField.transformationMethod = null
        visibilitySwitch.setImageResource(R.drawable.baseline_visibility_24)
    }

    override fun hidePassword() {
        passwordField.transformationMethod = PasswordTransformationMethod()
        visibilitySwitch.setImageResource(R.drawable.baseline_visibility_off_24)
    }
}
