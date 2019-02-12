package com.guulpay.more

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulexfinal.GuulexfinalActivity
import com.guulpay.mobilerecharge.MobileRechargeFragment
import com.guulpay.others.Constants
import kotlinx.android.synthetic.main.recharge_billpayment_layout.*

class MoreFragment:BaseFragment(), View.OnClickListener {


    companion object {
        const val CLASS_NAME = "MoreFragment"
        fun newInstance(): Fragment {
            return MoreFragment()
        }
    }
    override fun getLayoutView(): Int {
        return R.layout.more_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        tvmobilerecharge.setOnClickListener(this)
        tvforex.setOnClickListener(this)
        tvsavedcards.setOnClickListener(this)
        tvAyuuto.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if(v== tvmobilerecharge){
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, MobileRechargeFragment.CLASS_NAME)
            startActivity(intent)
        }
        if(v==tvforex)  {
            /*val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, GuulexFragment.CLASS_NAME)
            startActivity(intent)*/
            val intent = Intent(activity, GuulexfinalActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.GUULEX_BUY_TAB_POSITION)
            startActivity(intent)

        }
    }


}