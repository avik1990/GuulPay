package com.guulpay.helpSupport.submitReport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.calldetails_helpsupport_layout.*
import kotlinx.android.synthetic.main.help_support_submit_fragment.*
import kotlinx.android.synthetic.main.selected_order_helpsupport_layout.*


class HelpSupportSubmitFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val CLASS_NAME = "HelpSupportSubmitFragment"
        fun newInstance(): Fragment {
            return HelpSupportSubmitFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.help_support_submit_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        Utils.hideKeyboardOnOutsideTouch(view, activity as Activity)
        tvSelectOrder.visibility = View.GONE
        btnSave.isEnabled = false
        btnSave.alpha = 0.5F
        tvCountryCodeHelpSupport.setOnClickListener(this)
        CustomTextWatcher(etEmailHelpSupport, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 6) {
                    btnSave.isEnabled = true
                    btnSave.alpha = 1.0F
                } else {
                    btnSave.isEnabled = false
                    btnSave.alpha = 0.5F
                }
            }
        })


    }

    override fun initListeners() {
        imgvwCallHelpSupport.setOnClickListener(this)
    }

    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == imgvwCallHelpSupport) {
            Utils.setPhoneCall(tvCallNumberHelpSupport.text.toString(), context)
        } else if (v == tvCountryCodeHelpSupport) {
            val intent = Intent(activity, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
            countryCode?.let { tvCountryCodeHelpSupport.text = countryCode }
        }
    }
}