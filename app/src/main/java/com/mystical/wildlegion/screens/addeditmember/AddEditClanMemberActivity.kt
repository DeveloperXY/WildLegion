package com.mystical.wildlegion.screens.addeditmember

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mystical.wildlegion.R
import com.mystical.wildlegion.screens.CrudActivity
import kotlinx.android.synthetic.main.activity_add_edit_clan_member.*


class AddEditClanMemberActivity : CrudActivity(), AddEditClanMemberContract.View {

    private lateinit var mPresenter: AddEditClanMemberPresenter

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, R.layout.activity_add_edit_clan_member)

        mPresenter = AddEditClanMemberPresenter(this)
        mPresenter.start(intent)
    }

    override fun initializeActionBar() {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setActivityCheckboxListener() {
        activityCheckbox.setOnCheckedChangeListener { _, isChecked ->
            activityCheckbox.text = if (isChecked) "is active" else "is inactive"
        }
    }

    override fun setActivity(state: Boolean) {
        activityCheckbox.isChecked = state
    }

    override fun setActionbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setupRanksSpinner() {
        val spinnerAdapter = ArrayAdapter(supportActionBar!!.themedContext,
                R.layout.spinner_list_style,
                resources.getStringArray(R.array.ranks_array))

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        rankSpinner.adapter = spinnerAdapter
    }

    override fun getContext() = this

    override fun getBottomSheetMessage() = "Are you sure that you want to remove this member from the clan ?"

    override fun onDeleteConfirmed() {
        mPresenter.deleteClanMember()
    }

    override fun onBackPressed() {
        mPresenter.goBack()
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
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

    override fun showMissingNickname() {
        Toast.makeText(this, "A nickname is required.", Toast.LENGTH_LONG).show()
    }

    override fun showMissingIdentifier() {
        Toast.makeText(this, "An ID is required.", Toast.LENGTH_LONG).show()
    }

    fun onSaveClanMember(view: View?) {
        val nickname = nicknameField.text.toString()
        val identifier = identifierField.text.toString()
        val rank = rankSpinner.selectedItem.toString().substring(0, 1)
        val isActive = activityCheckbox.isChecked
        mPresenter.saveClanMember(nickname, identifier, rank, isActive)
    }

    override fun setNickname(nickname: String) {
        nicknameField.setText(nickname)
    }

    override fun setGamerangerId(gamerangerId: String) {
        identifierField.setText(gamerangerId)
    }

    override fun setRank(selection: Int) {
        rankSpinner.setSelection(selection, true)
    }

    override fun showSaveButton() {
        saveButton.visibility = VISIBLE
    }

    override fun hideSaveButton() {
        saveButton.visibility = GONE
    }
}
