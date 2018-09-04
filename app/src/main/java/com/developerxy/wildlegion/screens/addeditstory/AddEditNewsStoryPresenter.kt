package com.developerxy.wildlegion.screens.addeditstory

import android.content.Intent
import com.developerxy.wildlegion.di.components.DaggerAddEditNewsStoryPresenterComponent
import com.developerxy.wildlegion.di.modules.RetrofitModule
import com.developerxy.wildlegion.network.WixAPI
import com.developerxy.wildlegion.network.models.DeleteRequest
import com.developerxy.wildlegion.network.models.EditStoryRequest
import com.developerxy.wildlegion.network.models.NewStoryRequest
import com.developerxy.wildlegion.screens.main.models.News
import com.developerxy.wildlegion.utils.ResultCodes.Companion.NEWS_ADDED
import com.developerxy.wildlegion.utils.ResultCodes.Companion.NEWS_DELETED
import com.developerxy.wildlegion.utils.ResultCodes.Companion.NEWS_UPDATED
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddEditNewsStoryPresenter(var mView: AddEditNewsStoryContract.View) : AddEditNewsStoryContract.Presenter {

    @Inject
    lateinit var mWixAPI: WixAPI
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private var currentMemberPosition = -1
    private var isProcessing = false
    private var isEditing = false
    private var currentNews: News? = null

    init {
        DaggerAddEditNewsStoryPresenterComponent.builder()
                .retrofitModule(RetrofitModule())
                .build()
                .inject(this)
    }

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
            val request = NewStoryRequest(title, date, newsStory)
            addNewStory(request)
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
                            handleProcessingCompletion("Post successful.", NEWS_ADDED)
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