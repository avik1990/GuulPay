package com.guulpay.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addReceiver.reciever.RecieversFragment
import com.guulpay.changePasscode.ChangePasscodeFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.helpSupport.selectOrder.HelpSupportSelectOrderFragment
import com.guulpay.login.LoginActivity
import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.myProfile.view.ViewProfileFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.savedBanks.SavedBanksFragment
import com.guulpay.savedCards.SavedCardsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.myprofile_view_fragment.*
import org.jetbrains.anko.alert

class AccountFragment : BaseFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    var userDetailsResponse:UserDetailsResponse.User? = null

    companion object {
        const val CLASS_NAME = "AccountFragment"
        fun newInstance(): Fragment {
            return AccountFragment()
        }
    }

    var appdata: AppData? = null

    override fun getLayoutView(): Int {
        return R.layout.account_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appdata = AppData(context!!, Constants.Keys._KeyCryptoPreference)
        tvAvailablebalance.text=appdata!!.register_amount
        tvCurrencySymbolAccount.text=appdata!!.currency_symbol
        tvNameAccount.text=appdata!!.register_name
        tvPhoneAccount.text="+"+appdata!!.country_call_prifix+" "+appdata!!.register_phone
        tvEmailAccount.text=appdata!!.register_email
        Log.v("user_image",appdata!!.profile_image)
       // imgvwProfAccount.setImageURI(appdata!!.profile_image)
        try {
            Picasso.get().load(appdata!!.profile_image)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgvwProfAccount)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun initListeners() {
        btLogout.setOnClickListener(this)
        tvChangePasscode.setOnClickListener(this)
        tvCards.setOnClickListener(this)
        tvReceivers.setOnClickListener(this)
        tvAccounts.setOnClickListener(this)
        tvRaiseComplaint.setOnClickListener(this)
        cardvwProfileAccFrag.setOnClickListener(this)
        switchButtonFingerprint.setOnCheckedChangeListener(this)
        Log.d("IsSelected", "" + appdata!!.isFingerPrint)
        switchButtonFingerprint.isChecked = appdata!!.isFingerPrint
    }

    override fun onClick(v: View?) {
        //AddReceiverAccountFragment
        if (v == tvReceivers) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, RecieversFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == tvAccounts) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, SavedBanksFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == cardvwProfileAccFrag) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, ViewProfileFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == tvRaiseComplaint) {
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, HelpSupportSelectOrderFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, false, HelpSupportSelectOrderFragment.CLASS_NAME)
        } else if (v == tvCards) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, SavedCardsFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == tvChangePasscode) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, ChangePasscodeFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == btLogout) {
            showConfirmAlert()
        }
    }

    /* Confirm alert dialog before Logout */
    private fun showConfirmAlert() {
        activity?.alert(R.string.areYouSure, R.string.logout) {
            isCancelable = false
            negativeButton(R.string.no) {
                it.dismiss()
            }
            positiveButton(R.string.yes) {
                appdata!!.cleardata(context!!, Constants.Keys._KeyCryptoPreference)
                val intent = Intent(context, LoginActivity::class.java)
                activity!!.startActivity(intent)
                activity!!.finishAffinity()
            }
        }?.show()
    }

    /* For Fingerprint toggle button listener */
    override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        appdata!!.isFingerPrint = isChecked

    }


}