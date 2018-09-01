package com.developerxy.wildlegion.screens.addmember

import com.developerxy.wildlegion.network.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.NewClanMemberRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddClanMemberPresenter(var mView: AddClanMemberContract.View) : AddClanMemberContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI
    var isProcessing = false

    init {
        DaggerAddClanMemberPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

    override fun start() {
        mView.initializeActionBar()
        mView.setupRanksSpinner()
    }

    override fun goBack() {
        if (!isProcessing)
            mView.exit()
    }

    override fun addNewClanMember(nickname: String, gamerangerId: String, rank: String) {
        if (nickname.isEmpty()) {
            mView.showMissingNickname()
            return
        }

        if (gamerangerId.isEmpty()) {
            mView.showMissingIdentifier()
            return
        }

        val request = NewClanMemberRequest(nickname, gamerangerId, rank)
        mWixAPI.createNewClanMember(request)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isProcessing = true
                    mView.setLoadingStatement("Adding new clan member...")
                    mView.showLoadingView()
                }
                .subscribeBy(
                        onComplete = {
                            Observable.timer(1, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe {
                                        mView.setLoadingStatement("$nickname was successfully added to the clan.")
                                        mView.stopLoadingView()
                                    }
                                    .subscribe {
                                        isProcessing = false
                                        mView.exit()
                                    }
                        },
                        onError = {
                            isProcessing = false
                            mView.setLoadingStatement("")
                            mView.hideLoadingView()

                            (it as HttpException).apply {
                                val jsonObj = Gson().fromJson(response().errorBody()?.string(), JsonObject::class.java)
                                mView.onCreateMemberFailed(jsonObj.get("message").asString)
                            }
                        }
                )
    }
}