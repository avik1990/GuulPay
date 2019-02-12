package com.guulpay.viewAllRecentTransactions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.viewall_recent_transactions_fragment.*

class ViewAllRecentTransFragment:BaseFragment() {

    lateinit var viewAllRecentTransAdapter: ViewAllRecentTransAdapter
    companion object {
        const val CLASS_NAME = "ViewAllRecentTransFragment"
        fun newInstance(): Fragment {
            return ViewAllRecentTransFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.viewall_recent_transactions_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        /* View all Recent Transactions list */
        viewAllRecentTransAdapter = ViewAllRecentTransAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwAllRecentTrans.layoutManager = mLayoutManager
        recyclerVwAllRecentTrans.itemAnimator = DefaultItemAnimator()
        recyclerVwAllRecentTrans.adapter = viewAllRecentTransAdapter
    }
}