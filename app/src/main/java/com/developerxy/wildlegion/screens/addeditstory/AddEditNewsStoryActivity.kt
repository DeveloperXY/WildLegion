package com.developerxy.wildlegion.screens.addeditstory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.CrudActivity
import kotlinx.android.synthetic.main.activity_add_edit_news_story.*


class AddEditNewsStoryActivity : CrudActivity(), AddEditNewsStoryContract.View {

    private lateinit var mPresenter: AddEditNewsStoryPresenter

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, R.layout.activity_add_edit_news_story)

        mPresenter = AddEditNewsStoryPresenter(this)
        mPresenter.start(intent)
    }

    override fun initializeActionBar() {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setActionbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun getBottomSheetMessage() = "Are you sure that you want to remove this post from the news feed ?"

    override fun onDeleteConfirmed() {
        mPresenter.deleteNews()
    }

    override fun onBackPressed() {
        mPresenter.goBack()
    }

    override fun exit() {
        finish()
    }

    override fun exit(resultCode: Int, data: Intent?) {
        setResult(resultCode, data)
        finish()
    }

    override fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun showLoadingView() {
        fullscreenLoadingView.visibility = VISIBLE
    }

    override fun hideLoadingView() {
        fullscreenLoadingView.visibility = GONE
    }

    override fun setLoadingStatement(text: String) {
        fullscreenLoadingView.setStatement(text)
    }

    override fun stopLoadingView() {
        fullscreenLoadingView.stopLoading()
    }

    override fun showMissingTitle() {
        Toast.makeText(this, "A title is required.", Toast.LENGTH_LONG).show()
    }

    override fun showMissingDate() {
        Toast.makeText(this, "A date is required.", Toast.LENGTH_LONG).show()
    }

    override fun showMissingStory() {
        Toast.makeText(this, "Please type in your story's body.", Toast.LENGTH_LONG).show()
    }

    fun onSaveStory(view: View?) {
        val title = titleField.text.toString()
        val date = dateField.text.toString()
        val story = newsStoryField.text.toString()
        mPresenter.saveNewsStory(title, date, story)
    }

    override fun setTitle(title: String) {
        titleField.setText(title)
    }

    override fun setPostDate(postDate: String) {
        dateField.setText(postDate)
    }

    override fun setNewsStory(newsStory: String) {
        newsStoryField.setText(newsStory)
    }

    override fun showSaveButton() {
        saveButton.visibility = VISIBLE
    }

    override fun hideSaveButton() {
        saveButton.visibility = GONE
    }
}
