package com.developerxy.wildlegion.screens.main.fragments.members


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developerxy.wildlegion.R
import com.developerxy.wildlegion.screens.main.adapters.MembersAdapter
import com.developerxy.wildlegion.screens.main.network.WixAPI
import com.developerxy.wildlegion.utils.SpacesItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_members.*
import javax.inject.Inject

class MembersFragment : Fragment() {

    @Inject
    lateinit var mWixAPI: WixAPI

    lateinit var mMembersAdapter: MembersAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerMembersComponent.builder()
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMembersAdapter = MembersAdapter(context!!, mutableListOf())
        membersRecyclerview.layoutManager = LinearLayoutManager(activity)
        membersRecyclerview.addItemDecoration(SpacesItemDecoration(dpToPx(8)))
        membersRecyclerview.adapter = mMembersAdapter

        mWixAPI.getClanMembers()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mMembersAdapter.items = it
                    mMembersAdapter.notifyDataSetChanged()
                }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}
