package com.guulpay.savedBanks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addBankAccount.AddBankAccountFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.saved_banks.*

class SavedBanksFragment: BaseFragment(), View.OnClickListener {

    lateinit var savedListAdapter: SavedListAdapter
    companion object {
        const val CLASS_NAME = "SavedBanksFragment"
        fun newInstance(fromWhichFragment:String): Fragment {
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            val fragment = SavedBanksFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.saved_banks
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        val fragmentName = arguments?.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        val hideIcon = fragmentName?.equals(AddBankAccountFragment.CLASS_NAME)
        if (hideIcon!!){
            imgvwAddBank.visibility = View.GONE
        }
        else{
            imgvwAddBank.visibility = View.VISIBLE
        }
        /* Saved banks list */
        savedListAdapter = SavedListAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwSavedBankList.layoutManager = mLayoutManager
        recyclerVwSavedBankList.itemAnimator = DefaultItemAnimator()
        recyclerVwSavedBankList.adapter = savedListAdapter
    }

    override fun initListeners() {
        imgvwAddBank.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwAddBank){
            (activity as HomeActivity).setUpScreenUiForFragment(AddBankAccountFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddBankAccountFragment.newInstance(CLASS_NAME),
                    R.id.flFragmentContainerHome, true, AddBankAccountFragment.CLASS_NAME)
        }
    }
}