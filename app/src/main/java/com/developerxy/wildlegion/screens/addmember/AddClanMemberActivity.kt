package com.developerxy.wildlegion.screens.addmember

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
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
}
