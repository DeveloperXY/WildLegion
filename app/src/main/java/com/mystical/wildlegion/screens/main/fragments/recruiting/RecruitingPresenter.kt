package com.mystical.wildlegion.screens.main.fragments.recruiting

import com.mystical.wildlegion.network.WixAPI
import com.mystical.wildlegion.utils.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RecruitingPresenter(var mView: RecruitingContract.View) : RecruitingContract.Presenter {

    private var mWixAPI = ServiceGenerator.createService(WixAPI::class.java)

    override fun start() {
        mView.setListenerOnGuestBookButton()
        mView.showRecruitmentDescription()
        mWixAPI.getRecruitmentStatus()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            val description = getRecruitmentDescription(it.isOpen)
                            mView.setRecruitmentDescription(description)
                        },
                        onError = {
                            mView.setRecruitmentDescription("Recruitment data is unavailable.")
                        }
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