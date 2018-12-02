package com.mystical.wildlegion.screens.login

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class LoginPresenterTest {
    @Mock
    lateinit var mView: LoginContract.View
    @Mock
    lateinit var mFirebaseAuth: FirebaseAuth
    @Mock
    lateinit var mFirebaseAuthTask: Task<AuthResult>
    @Captor
    lateinit var loginButtonTextCaptor: ArgumentCaptor<String>
    @Captor
    lateinit var onCompleteCaptor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    lateinit var mPresenter: LoginPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = LoginPresenter(mView, mFirebaseAuth)

        `when`(mFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mFirebaseAuthTask)
        `when`(mFirebaseAuthTask.addOnCompleteListener(onCompleteCaptor.capture()))
                .thenReturn(mFirebaseAuthTask)
    }

    @Test
    fun necessaryViewInitializationIsPerformedOnStartup() {
        mPresenter.start()

        verify(mView).displayWlLogo()
        verify(mView).displayShcBackground()
        verify(mView).setListenerOnLoginButton()
        verify(mView).setListenerOnVisibilitySwitch()
    }

    @Test
    fun invalidLoginCredentialsShowErrorToast() {
        `when`(mFirebaseAuthTask.isSuccessful).thenReturn(false)

        mPresenter.logUserIn("email", "password")
        verify(mView).showProgressBar()
        verify(mView).disableLoginButton()
        verify(mFirebaseAuth).signInWithEmailAndPassword(eq("email"), eq("password"))

        onCompleteCaptor.value.onComplete(mFirebaseAuthTask)
        verify(mView).hideProgressBar()
        verify(mView).enableLoginButton()
        verify(mView, times(2)).setLoginButtonText(loginButtonTextCaptor.capture())
        verify(mView).showLoginError()

        assertEquals("Signing you in...", loginButtonTextCaptor.allValues[0])
        assertEquals("Sign in", loginButtonTextCaptor.allValues[1])
    }

    @Test
    fun openMainActivityIfLoginWasSuccessful() {
        `when`(mFirebaseAuthTask.isSuccessful).thenReturn(true)

        mPresenter.logUserIn("email", "password")
        verify(mView).showProgressBar()
        verify(mView).disableLoginButton()
        verify(mFirebaseAuth).signInWithEmailAndPassword(eq("email"), eq("password"))

        onCompleteCaptor.value.onComplete(mFirebaseAuthTask)
        verify(mView).openMainActivity()
    }

    @Test
    fun requestToRevealPasswordDoesRevealIt() {
        mPresenter.togglePasswordVisibility()
        verify(mView).showPassword()
    }

    @Test
    fun requestToHidePasswordDoesHideIt() {
        mPresenter.isPasswordRevealed = true
        mPresenter.togglePasswordVisibility()
        verify(mView).hidePassword()
    }

    @Test
    fun noInteractionWithViewIfOneOfTheCredentialsIsEmpty() {
        mPresenter.logUserIn("m.a.zouag@gmail.com", "")
        verifyZeroInteractions(mView)
    }
}