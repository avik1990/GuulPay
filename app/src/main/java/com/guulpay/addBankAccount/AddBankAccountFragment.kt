package com.guulpay.addBankAccount

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.countryWiseBanks.CountryWiseBankListActivity
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.savedBanks.SavedBanksFragment
import kotlinx.android.synthetic.main.addbankaccount_fragment.*
import kotlinx.android.synthetic.main.bankaccount_layout.*
import kotlinx.android.synthetic.main.guulex_final_buy_fragment.*

class AddBankAccountFragment : BaseFragment(), View.OnClickListener {

    var flagfrom: Int = 0
    companion object {
        const val CLASS_NAME = "AddBankAccountFragment"
        fun newInstance(fromWhichFragment: String): Fragment {
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            val fragment = AddBankAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    /* val intent = Intent(this, CountryCodesListActivity::class.java)
     startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)*/

    override fun getLayoutView(): Int {
        return R.layout.addbankaccount_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        val fragmentName = arguments?.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        val hideIcon = fragmentName?.equals(SavedBanksFragment.CLASS_NAME)
        if (hideIcon!!) {
            btAddedBankList.visibility = View.GONE
        } else {
            btAddedBankList.visibility = View.GONE
        }
        btSaveAddBank.isEnabled = false
        btSaveAddBank.alpha = 0.5F

        CustomTextWatcher(etName, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 3) {
                    btSaveAddBank.isEnabled = true
                    btSaveAddBank.alpha = 1.0F

                } else {
                    btSaveAddBank.isEnabled = false
                    btSaveAddBank.alpha = 0.5F
                }
            }
        })
        CustomTextWatcher(etAccountno, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 3) {
                    btSaveAddBank.isEnabled = true
                    btSaveAddBank.alpha = 1.0F

                } else {
                    btSaveAddBank.isEnabled = false
                    btSaveAddBank.alpha = 0.5F
                }
            }
        })
        CustomTextWatcher(etIfsccode, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 3) {
                    btSaveAddBank.isEnabled = true
                    btSaveAddBank.alpha = 1.0F

                } else {
                    btSaveAddBank.isEnabled = false
                    btSaveAddBank.alpha = 0.5F
                }
            }
        })
        CustomTextWatcher(etIbanNumber, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 3) {
                    btSaveAddBank.isEnabled = true
                    btSaveAddBank.alpha = 1.0F

                } else {
                    btSaveAddBank.isEnabled = false
                    btSaveAddBank.alpha = 0.5F
                }
            }
        })
        //  CustomTextWatcher
        // etName
    }

    override fun initListeners() {
        btSaveAddBank.setOnClickListener(this)
        tvDomestic.setOnClickListener(this)
        tvInternational.setOnClickListener(this)
        btAddedBankList.setOnClickListener(this)
        tvBank.setOnClickListener(this)
        tvSelectedCountry.setOnClickListener(this)
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
        } else if (v == btSaveAddBank) {
            Utils.hideKeyboard(activity)
            Toast.makeText(activity, "Bank added successfully", Toast.LENGTH_SHORT).show()
            activity?.finish()
        } else if (v == btAddedBankList) {
            (activity as HomeActivity).setUpScreenUiForFragment(SavedBanksFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, SavedBanksFragment.newInstance(CLASS_NAME),
                    R.id.flFragmentContainerHome, true, SavedBanksFragment.CLASS_NAME)
        } else if (v == tvBank) {
            val intent = Intent(activity, CountryWiseBankListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_BANK)
        }
        else if(v == tvSelectedCountry){
            val intent = Intent(activity, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_BANK) {
            val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_BANK_CODE)
            countryCode?.let { tvBank.text = countryCode }
        }
        else if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            flagfrom = Constants.RequestCodes.REQUEST_CODE_COUNTRY
            try {
                val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
                //countryCode?.let { tvspinnerCountryCode.text = countryCode }
//                countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)!!
//                countryCode?.let { Log.d("CountryCodeID", countryCodeID) }
//
//                currencyCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CURRENCYCODE)!!
//                currencyCode?.let { Log.d("currencyCode", currencyCode) }

                //Added Rishabh 28/1/2019 */

                val countryCodeName = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE_NAME)
                countryCodeName?.let { tvSelectedCountry.text = countryCodeName }
            } catch (e: Exception) { }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
//            flagfrom = Constants.RequestCodes.REQUEST_CODE_COUNTRY
//            try {
//                val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
//                //countryCode?.let { tvspinnerCountryCode.text = countryCode }
//                countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)!!
//                countryCode?.let { Log.d("CountryCodeID", countryCodeID) }
//
//                currencyCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CURRENCYCODE)!!
//                currencyCode?.let { Log.d("currencyCode", currencyCode) }
//
//                //Added Rishabh 28/1/2019 */
//
//                val countryCodeName = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE_NAME)
//                countryCodeName?.let { tvspinnerCountryCode.text = countryCodeName }
//            } catch (e: Exception) { }
//        }
//    }

}