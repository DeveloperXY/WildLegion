package com.mystical.wildlegion.screens.addeditstory

import android.content.Intent
import com.google.firebase.database.FirebaseDatabase
import com.mystical.wildlegion.data.UserRepository
import com.mystical.wildlegion.network.WixAPI
import com.mystical.wildlegion.network.models.DeleteRequest
import com.mystical.wildlegion.network.models.EditStoryRequest
import com.mystical.wildlegion.network.models.NewStoryRequest
import com.mystical.wildlegion.screens.main.models.News
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_ADDED
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_DELETED
import com.mystical.wildlegion.utils.ResultCodes.Companion.NEWS_UPDATED
import com.mystical.wildlegion.utils.ServiceGenerator
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddEditNewsStoryPresenter(var mView: AddEditNewsStoryContract.View) : AddEditNewsStoryContract.Presenter {

    private var mWixAPI = ServiceGenerator.createService(WixAPI::class.java)
    private var mUserRepository = UserRepository.getInstance(mView.getContext())
    private val mFirebaseDatabase = FirebaseDatabase.getInstance()

    override fun getUserRepository() = mUserRepository

    private var currentMemberPosition = -1
    private var isProcessing = false
    private var isEditing = false
    private var currentNews: News? = null

    override fun start() {
        mView.initializeActionBar()
    }

    override fun start(intent: Intent) {
        start()

        isEditing = intent.getBooleanExtra("isEditing", false)
        if (isEditing) {
            currentMemberPosition = intent.getIntExtra("position", -1)
            mView.setActionbarTitle("Edit news story")
            currentNews = intent.getSerializableExtra("news") as News?
            mView.setTitle(currentNews?.title!!)
            mView.setPostDate(currentNews?.postDate!!)
            mView.setNewsStory(currentNews?.newsStory!!)
        } else {
            mView.setActionbarTitle("Create a new post")
            val chained = intent.getBooleanExtra("chained", false)
            if (chained) {
                val rank = intent.getStringExtra("rank")
                val gamerangerId = intent.getStringExtra("gamerangerId")
                val nickname = intent.getStringExtra("nickname")

                mView.setTitle("New " +
                        when (rank) {
                            "A" -> "academy"
                            "M" -> "medium"
                            else -> "expert"
                        } + " player")
                mView.setPostDate(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()))
                mView.setNewsStory("$nickname (ID $gamerangerId) joined WL as " +
                        when (rank) {
                            "A" -> "an academy member"
                            "M" -> "a medium member"
                            else -> "an expert"
                        } + ". Welcome !")
            }
        }
    }

    override fun goBack() {
        if (!isProcessing)
            mView.exit()
    }

    override fun saveNewsStory(title: String, date: String, newsStory: String) {
        if (title.isEmpty()) {
            mView.showMissingTitle()
            return
        }

        if (date.isEmpty()) {
            mView.showMissingDate()
            return
        }

        if (newsStory.isEmpty()) {
            mView.showMissingStory()
            return
        }

        if (isEditing) {
            currentNews?.title = title
            currentNews?.postDate = date
            currentNews?.newsStory = newsStory
            val request = EditStoryRequest(currentNews!!)
            editStory(request)
        } else {
            mUserRepository.getCurrentUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val request = NewStoryRequest(title, date, newsStory, it.nickname)
                        addNewStory(request)
                    }
        }
    }

    override fun deleteNews() {
        if (currentNews != null) {
            val news = currentNews!!
            mWixAPI.deleteNews(DeleteRequest(news._id))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        onSubscribe("Deleting story...")
                    }
                    .subscribeBy(
                            onComplete = {
                                val data = Intent()
                                data.putExtra("position", currentMemberPosition)
                                handleProcessingCompletion("Story successfully deleted.",
                                        NEWS_DELETED, data)
                            },
                            onError = this::handleProcessingError
                    )
        }
    }

    private fun addNewStory(request: NewStoryRequest) {
        mWixAPI.createNewStory(request)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe("Publishing post...")
                }
                .subscribeBy(
                        onComplete = {
                            mFirebaseDatabase.reference
                                    .child("News")
                                    .push()
                                    .setValue(request)
                                    .addOnSuccessListener {
                                        handleProcessingCompletion("Post successful.", NEWS_ADDED)
                                    }
                        },
                        onError = this::handleProcessingError
                )
    }

    private fun editStory(request: EditStoryRequest) {
        mWixAPI.editNews(request)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe("Updating post...")
                }
                .subscribeBy(
                        onComplete = {
                            handleProcessingCompletion("Post successfully updated.", NEWS_UPDATED)
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

        if (throwable is UnknownHostException)
            mView.showErrorMessage("No internet connection.")
        else {
            (throwable as HttpException).apply {
                val jsonObj = Gson().fromJson(response().errorBody()?.string(), JsonObject::class.java)
                val errorMessage = if (jsonObj == null) "Unexpected error." else
                    jsonObj.get("message").asString
                mView.showErrorMessage(errorMessage)
            }
        }
    }

    private fun onSubscribe(loadingStatement: String) {
        isProcessing = true
        mView.hideSaveButton()
        mView.setLoadingStatement(loadingStatement)
        mView.showLoadingView()
    }
}