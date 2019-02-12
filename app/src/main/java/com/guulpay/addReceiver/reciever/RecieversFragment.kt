package com.guulpay.addReceiver.reciever

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addReceiver.AddReceiverAccountFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.saved_banks.*

class RecieversFragment : BaseFragment(), View.OnClickListener {

    lateinit var savedListAdapter: RecieverListAdapter

    companion object {
        const val CLASS_NAME = "RecieversFragment"
        fun newInstance(): Fragment {
            return RecieversFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.reciever_list
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        savedListAdapter = RecieverListAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwSavedBankList.layoutManager = mLayoutManager
        recyclerVwSavedBankList.itemAnimator = DefaultItemAnimator()
        recyclerVwSavedBankList.adapter = savedListAdapter
    }

    override fun initListeners() {
        imgvwAddBank.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwAddBank) {
            (activity as HomeActivity).setUpScreenUiForFragment(AddReceiverAccountFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddReceiverAccountFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, AddReceiverAccountFragment.CLASS_NAME)
        }
    }
}