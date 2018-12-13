package com.mystical.wildlegion.screens.main.fragments.recruiting

class RecruitingPresenter(var mView: RecruitingContract.View) : RecruitingContract.Presenter {

    override fun start() {
        mView.displayGuestbook("http://users4.smartgb.com/g/g.php?a=s&i=g44-71854-e8")
    }
}