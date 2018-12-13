package com.mystical.wildlegion.screens.main.fragments.recruiting


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mystical.wildlegion.R
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

    override fun displayGuestbook(url: String) {
        mWebView.loadUrl(url)
    }
}
