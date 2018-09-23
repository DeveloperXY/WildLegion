package com.developerxy.wildlegion.screens.main.fragments.recruiting

import com.developerxy.wildlegion.di.components.DaggerRecruitingPresenterComponent
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecruitingPresenter(var mView: RecruitingContract.View) : RecruitingContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI

    init {
        DaggerRecruitingPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

    override fun start() {
        mWixAPI.getRecruitmentStatus()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            val description = getRecruitmentDescription(it.isOpen)
                            mView.setRecruitmentDescription(description)
                            mView.showRecruitmentDescription()
                            mView.showGuestbookButton()

                        },
                        onError = {
                            mView.showErrorMessage()
                            mView.hideGuestbookButton()
                        },
                        onComplete = mView::hideProgressbar
                )
    }

    private fun getRecruitmentDescription(isRecruitmentOpen: Boolean) =
            if (isRecruitmentOpen)
                "Recruitment in -=W.L=- is now open.\n\nFirst you must read our rules. " +
                        "Click the \"About Clan\" tab for rules. If you think you can follow all rules " +
                        "only then leave your name in the guestbook below.\n\n" +
                        "In guestbook write down your real name, nickname in gr, " +
                        "account id, your country, your age and your previous clan.\n\n" +
                        "Afterwards you must play test game with Founder or Leader or Council. " +
                        "Then we will decide whether we will take you or not."
            else "Recruitment in -=W.L=- is now closed."
}