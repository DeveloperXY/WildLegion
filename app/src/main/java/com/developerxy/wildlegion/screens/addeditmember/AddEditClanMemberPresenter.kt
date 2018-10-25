package com.developerxy.wildlegion.screens.addeditmember

import android.content.Intent
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.DeleteRequest
import com.developerxy.wildlegion.network.models.EditClanMemberRequest
import com.developerxy.wildlegion.network.models.NewClanMemberRequest
import com.developerxy.wildlegion.screens.main.models.Member
import com.developerxy.wildlegion.utils.ResultCodes
import com.developerxy.wildlegion.utils.ResultCodes.Companion.MEMBER_ADDED
import com.developerxy.wildlegion.utils.ResultCodes.Companion.MEMBER_DELETED
import com.developerxy.wildlegion.utils.ResultCodes.Companion.MEMBER_UPDATED
import com.developerxy.wildlegion.utils.ServiceGenerator
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class AddEditClanMemberPresenter(var mView: AddEditClanMemberContract.View) : AddEditClanMemberContract.Presenter {

    private var mWixAPI = ServiceGenerator.createService(WixAPI::class.java)
    private var isProcessing = false
    private var isEditing = false
    private var currentMemberPosition = -1
    private var currentMember: Member? = null

    override fun start() {
        mView.initializeActionBar()
        mView.setupRanksSpinner()
    }

    override fun start(intent: Intent) {
        start()

        isEditing = intent.getBooleanExtra("isEditing", false)
        if (isEditing) {
            currentMemberPosition = intent.getIntExtra("position", -1)
            mView.setActionbarTitle("Edit clan member info")
            currentMember = intent.getSerializableExtra("member") as Member
            mView.setNickname(currentMember?.nickname!!)
            mView.setGamerangerId(currentMember?.gamerangerId!!)

            val selection = when (currentMember?.rank!!) {
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

        if (isEditing) {
            currentMember?.nickname = nickname
            currentMember?.gamerangerId = gamerangerId
            currentMember?.rank = rank.toCharArray()[0]
            val request = EditClanMemberRequest(currentMember!!)
            editClanMember(request)
        } else {
            val request = NewClanMemberRequest(nickname, gamerangerId, rank)
            addNewClanMember(request)
        }
    }

    override fun deleteClanMember() {
        val member = currentMember!!
        mWixAPI.removeClanMember(DeleteRequest(member._id))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe("Deleting...")
                }
                .subscribeBy(
                        onComplete = {
                            val data = Intent()
                            data.putExtra("position", currentMemberPosition)
                            handleProcessingCompletion("${member.nickname} was removed from the clan.",
                                    MEMBER_DELETED, data)
                        },
                        onError = this::handleProcessingError
                )
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
                            val data = Intent()
                            data.putExtra("nickname", request.nickname)
                            data.putExtra("gamerangerId", request.gamerangerId)
                            data.putExtra("rank", request.rank)
                            handleProcessingCompletion(
                                    "${request.nickname} was successfully added to the clan.",
                                    MEMBER_ADDED, data)
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
                            handleProcessingCompletion("${request.nickname}'s info were successfully updated.",
                                    MEMBER_UPDATED)
                        },
                        onError = this::handleProcessingError
                )
    }

    private fun handleProcessingCompletion(successMessage: String, resultCode: Int, data: Intent? = null) {
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mView.setLoadingStatement(successMessage)
                    mView.stopLoadingView()
                }
                .subscribe {
                    isProcessing = false
                    mView.exit(resultCode, data)
                }
    }

    private fun handleProcessingError(throwable: Throwable) {
        isProcessing = false
        mView.setLoadingStatement("")
        mView.hideLoadingView()
        mView.showSaveButton()

        (throwable as HttpException).apply {
            val jsonObj = Gson().fromJson(response().errorBody()?.string(), JsonObject::class.java)
            val errorMessage = if (jsonObj == null) "Unexpected error." else
                jsonObj.get("message").asString
            mView.showErrorMessage(errorMessage)
        }
    }

    private fun onSubscribe(loadingStatement: String) {
        isProcessing = true
        mView.hideSaveButton()
        mView.setLoadingStatement(loadingStatement)
        mView.showLoadingView()
    }
}