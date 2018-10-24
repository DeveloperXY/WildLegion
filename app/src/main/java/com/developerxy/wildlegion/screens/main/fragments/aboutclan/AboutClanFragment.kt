package com.developerxy.wildlegion.screens.main.fragments.aboutclan


import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developerxy.wildlegion.App
import com.developerxy.wildlegion.R
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.fragment_about_clan.*


class AboutClanFragment : Fragment(), AboutClanContract.View {

    private lateinit var mPresenter: AboutClanPresenter
    private var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter = AboutClanPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_clan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBodyText()
        youtubePlayerFragment = activity?.supportFragmentManager?.findFragmentById(R.id.mYoutubeContainer)
                as YouTubePlayerSupportFragment?
        if (youtubePlayerFragment == null) {
            youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
            childFragmentManager.beginTransaction().add(R.id.mYoutubeContainer, youtubePlayerFragment).commit()
        }

        val progressDrawable = mProgressbar.indeterminateDrawable.mutate()
        progressDrawable.setColorFilter(resources.getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN)
        mProgressbar.indeterminateDrawable = progressDrawable
    }

    override fun getContext() = activity!!

    private fun setBodyText() {
        mRedSection.text = getString(R.string.about_clan_part_1)
        mRedSection.append("\n\n")
        mRedSection.append(getString(R.string.about_clan_part_2))

        resources.getStringArray(R.array.rules)
                .forEachIndexed { index, rule ->
                    mRulesSection.append("${index + 1}. $rule\n")
                }
    }

    fun initializeYoutubePlayer() {
        mPresenter.initYoutubePlayer()
    }

    override fun showYoutubeContainer() {
        mYoutubeContainer.visibility = View.VISIBLE
    }

    override fun hidePlaceholder() {
        mPlaceholder.visibility = View.GONE
    }

    override fun hideProgressbar() {
        mProgressbar.visibility = View.GONE
    }

    override fun showRedSection() {
        mRedSection.visibility = View.VISIBLE
    }

    override fun showRulesSection() {
        mRulesSection.visibility = View.VISIBLE
    }

    override fun switchOrientationToPortrait() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun getYoutubeFragment() = youtubePlayerFragment

    override fun getApp() = (activity?.application) as App

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachYoutubePlayer()
    }
}
