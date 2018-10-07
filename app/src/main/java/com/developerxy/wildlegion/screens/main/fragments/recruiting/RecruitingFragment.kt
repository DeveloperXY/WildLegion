package com.developerxy.wildlegion.screens.main.fragments.recruiting


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.guestbook.GuestBookActivity
import kotlinx.android.synthetic.main.fragment_recruiting.*

class RecruitingFragment : Fragment(), RecruitingContract.View {

    lateinit var mPresenter: RecruitingPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter = RecruitingPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_recruiting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter.start()
    }

    override fun setRecruitmentDescription(text: String) {
        mRecruitmentOpenTextArea.text = text
    }

    override fun showRecruitmentDescription() {
        mScrollView.visibility = VISIBLE
    }

    override fun hideProgressbar() {
        progressBar.visibility = GONE
    }

    override fun showErrorMessage() {
        mErrorLabel.visibility = VISIBLE
    }

    override fun hideGuestbookButton() {
        openGuestBookBtn.visibility = GONE
    }

    override fun showGuestbookButton() {
        openGuestBookBtn.visibility = VISIBLE
    }

    override fun setListenerOnGuestBookButton() {
        openGuestBookBtn.setOnClickListener {
            startActivity(Intent(activity, GuestBookActivity::class.java))
        }
    }
}
