package com.guulpay.addReceiver

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.addreceiver_account_fragment.*
import kotlinx.android.synthetic.main.bankaccount_layout.*

class AddReceiverAccountFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val CLASS_NAME = "AddReceiverAccountFragment"
        fun newInstance(): Fragment {
            return AddReceiverAccountFragment()
        }
    }




    override fun onClick(v: View?) {
        if (v == tvDomestic) {
            tvDomestic.setTextColor(Color.WHITE)
            tvDomestic.background = resources.getDrawable(R.drawable.drawable_colorprimary_roundedcorner)
            tvInternational.background = null
            tvInternational.setTextColor(Color.DKGRAY)
            hideLayoutForDomesticBranch()
        } else if (v == tvInternational) {
            tvInternational.setTextColor(Color.WHITE)
            tvInternational.background = resources.getDrawable(R.drawable.drawable_colorprimary_roundedcorner)
            tvDomestic.background = null
            tvDomestic.setTextColor(Color.DKGRAY)
            visibleLayoutForInternationalBranch()
        } else if (v == btNext) {
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddReceiverPasscodeFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, AddReceiverPasscodeFragment.CLASS_NAME)
        }
    }

    private fun hideLayoutForDomesticBranch() {
        tvSelectCountryText.visibility = View.GONE
        tvSelectedCountry.visibility = View.GONE

        vwLineIbanNumber.visibility = View.GONE
        etIbanNumber.visibility = View.GONE
        tvIbanNumber.visibility = View.GONE
    }

    private fun visibleLayoutForInternationalBranch() {
        tvSelectCountryText.visibility = View.VISIBLE
        tvSelectedCountry.visibility = View.VISIBLE

        vwLineIbanNumber.visibility = View.VISIBLE
        etIbanNumber.visibility = View.VISIBLE
        tvIbanNumber.visibility = View.VISIBLE
    }

    override fun getLayoutView(): Int {
        return R.layout.addreceiver_account_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

        btAddedBankList.visibility = View.GONE // Required while adding bank accounts only
    }

    override fun initListeners() {
        tvDomestic.setOnClickListener(this)
        tvInternational.setOnClickListener(this)
        btNext.setOnClickListener(this)
    }
}