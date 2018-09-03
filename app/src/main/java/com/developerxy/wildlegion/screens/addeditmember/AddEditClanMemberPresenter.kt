package com.developerxy.wildlegion.screens.addeditmember

import android.content.Intent
import com.developerxy.wildlegion.network.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.EditClanMemberRequest
import com.developerxy.wildlegion.network.models.NewClanMemberRequest
import com.developerxy.wildlegion.screens.main.models.Member
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddEditClanMemberPresenter(var mView: AddEditClanMemberContract.View) : AddEditClanMemberContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI
    private var isProcessing = false
    private var isEditing = false
    private var currentMember: Member? = null

    init {
        DaggerAddEditClanMemberPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

    override fun start() {
        mView.initializeActionBar()
        mView.setupRanksSpinner()
    }

    override fun start(intent: Intent) {
        start()

        isEditing = intent.getBooleanExtra("isEditing", false)
        if (isEditing) {
            mView.setActionbarTitle("Edit clan member info")
            currentMember = intent.getSerializableExtra("member") as Member
            mView.setNickname(currentMember?.nickname!!)
            mView.setGamerangerId(currentMember?.gamerangerId!!)

            val selection = when(currentMember?.rank!!) {
                'A' -> 0
                'M' -> 1
                else -> 2
            }
            mView.setRank(selection)
        } else {
            mView.setActionbarTitle("Add a new clan member")
        }
    }

    override fun goBack() {
        if (!isProcessing)
            mView.exit()
    }

    override fun saveClanMember(nickname: String, gamerangerId: String, rank: String) {
        if (nickname.isEmpty()) {
            mView.showMissingNickname()
            return
        }

        if (gamerangerId.isEmpty()) {
            mView.showMissingIdentifier()
            return
        }

        currentMember?.nickname = nickname
        currentMember?.gamerangerId = gamerangerId
        currentMember?.rank = rank.toCharArray()[0]

        if (isEditing) {
            val request = EditClanMemberRequest(currentMember!!)
            editClanMember(request)
        } else {
            val request = NewClanMemberRequest(nickname, gamerangerId, rank)
            addNewClanMember(request)
        }
    }

    private fun addNewClanMember(request: NewClanMemberRequest) {
        mWixAPI.createNewClanMember(request)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe("Adding new clan member...")
                }
                .subscribeBy(
                        onComplete = {
                            handleProcessingCompletion("${request.nickname} was successfully added to the clan.")
                        },
                        onError = this::handleProcessingError
                )
    }

    private fun editClanMember(request: EditClanMemberRequest) {
        mWixAPI.editClanMember(request)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe("Updating info...")
                }
                .subscribeBy(
                        onComplete = {
                            handleProcessingCompletion("${request.nickname}'s info were successfully updated.")
                        },
                        onError = this::handleProcessingError
                )
    }

    private fun handleProcessingCompletion(successMessage: String) {
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mView.setLoadingStatement(successMessage)
                    mView.stopLoadingView()
                }
                .subscribe {
                    isProcessing = false
                    mView.exitAfterProcessing()
                }
    }

    private fun handleProcessingError(throwable: Throwable) {
        isProcessing = false
        mView.setLoadingStatement("")
        mView.hideLoadingView()

        (throwable as HttpException).apply {
            val jsonObj = Gson().fromJson(response().errorBody()?.string(), JsonObject::class.java)
            val errorMessage = if (jsonObj == null) "Unexpected error." else
                jsonObj.get("message").asString
            mView.showErrorMessage(errorMessage)
        }
    }

    private fun onSubscribe(loadingStatement: String) {
        isProcessing = true
        mView.setLoadingStatement(loadingStatement)
        mView.showLoadingView()
    }
}