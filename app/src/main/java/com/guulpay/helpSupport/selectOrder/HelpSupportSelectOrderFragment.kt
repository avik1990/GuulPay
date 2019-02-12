package com.guulpay.helpSupport.selectOrder

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.help_support_selectorder_fragment.*

class HelpSupportSelectOrderFragment: BaseFragment(){

    lateinit var helpSupportSelectOrderAdapter: HelpSupportSelectOrderAdapter
    companion object {
        const val CLASS_NAME = "HelpSupportSelectOrderFragment"
        fun newInstance(): Fragment {
            return HelpSupportSelectOrderFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.help_support_selectorder_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        /* Order list items */
        helpSupportSelectOrderAdapter = HelpSupportSelectOrderAdapter(activity as DashboardHomeActivity?)
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwOrderList.layoutManager = mLayoutManager
        recyclerVwOrderList.itemAnimator = DefaultItemAnimator()
        recyclerVwOrderList.adapter = helpSupportSelectOrderAdapter
    }

    override fun initListeners() {

    }
}