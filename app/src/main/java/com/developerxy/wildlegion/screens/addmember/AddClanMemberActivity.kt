package com.developerxy.wildlegion.screens.addmember

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.BackgroundActivity
import kotlinx.android.synthetic.main.activity_add_clan_member.*


class AddClanMemberActivity : BackgroundActivity(), AddClanMemberContract.View {

    private lateinit var mPresenter: AddClanMemberPresenter

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, R.layout.activity_add_clan_member)

        mPresenter = AddClanMemberPresenter(this)
        mPresenter.start()
    }

    override fun initializeActionBar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Add new clan member"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupRanksSpinner() {
        val spinnerAdapter = ArrayAdapter(supportActionBar!!.themedContext,
                R.layout.spinner_list_style,
                resources.getStringArray(R.array.ranks_array))

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        rankSpinner.adapter = spinnerAdapter
    }

    override fun onBackPressed() {
        mPresenter.goBack()
    }

    override fun exit() {
        super.onBackPressed()
    }

    override fun onCreateMemberFailed(errorMessage: String) {
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

    fun onCreateClanMember(view: View?) {
        val nickname = nicknameField.text.toString()
        if (nickname.isEmpty()) {
            Toast.makeText(this, "A nickname is required.", Toast.LENGTH_LONG).show()
            return
        }
        val identifier = identifierField.text.toString()
        if (identifier.isEmpty()) {
            Toast.makeText(this, "An ID is required.", Toast.LENGTH_LONG).show()
            return
        }
        val rank = rankSpinner.selectedItem.toString().substring(0, 1)

        mPresenter.addNewClanMember(nickname, identifier, rank)
    }
}
