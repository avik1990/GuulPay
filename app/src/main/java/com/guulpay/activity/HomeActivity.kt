package com.guulpay.activity

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guulpay.R
import com.guulpay.addBankAccount.AddBankAccountFragment
import com.guulpay.addMoney.addAmount.AddMoneyEnterAmountFragment
import com.guulpay.addMoney.completeYourPayment.CompleteYourPaymentFragment
import com.guulpay.addMoney.paymentStatus.AddMoneyPaymentStatusFragment
import com.guulpay.addReceiver.AddReceiverAccountFragment
import com.guulpay.addReceiver.reciever.RecieversFragment
import com.guulpay.basemodel.CurrenciesModel
import com.guulpay.changePasscode.ChangePasscodeFragment
import com.guulpay.contactus.ContactUsFragment
import com.guulpay.enterOtp.EnterOtpFragment
import com.guulpay.forgotPasscode.enterMobile.EnterMobileForgotPasscodeFragment
import com.guulpay.guulex.GuulexFragment
import com.guulpay.kycverification.KycFragment
import com.guulpay.login.LoginActivity
import com.guulpay.mobilerecharge.MobileRechargeFragment
import com.guulpay.mobilerechargehistory.MobileRechargeHistFragment
import com.guulpay.myProfile.edit.EditProfileFragment
import com.guulpay.myProfile.view.ViewProfileFragment
import com.guulpay.myWallet.autoLoad.AutoLoadAmountFragment
import com.guulpay.notification.NotificationFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.payments.send.paymentStatus.SendMoneyPaymentStatus
import com.guulpay.savedBanks.SavedBanksFragment
import com.guulpay.savedCards.SavedCardsFragment
import com.guulpay.termsAndConditions.TermsAndConditionsFragment
import com.guulpay.viewAllRecentTransactions.ViewAllRecentTransFragment
import kotlinx.android.synthetic.main.common_toolbar_layout.*
import kotlinx.android.synthetic.main.home_activity.*
import java.io.IOException

class HomeActivity : BaseActivity(), View.OnClickListener {

    var appData: AppData? = null
    var currenciesModellist: ArrayList<CurrenciesModel> = ArrayList()

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun onBackPressed() {
        Utils.hideKeyboard(this)
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is AddMoneyPaymentStatusFragment) {
                finish()
            } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is SendMoneyPaymentStatus) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is SavedBanksFragment) {
                    setUpScreenUiForFragment(AddBankAccountFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is AddBankAccountFragment) {
                    setUpScreenUiForFragment(SavedBanksFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is EditProfileFragment) {
                    setUpScreenUiForFragment(ViewProfileFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is CompleteYourPaymentFragment) {
                    setUpScreenUiForFragment(AddMoneyEnterAmountFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is AddReceiverAccountFragment) {
                    setUpScreenUiForFragment(RecieversFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is MobileRechargeHistFragment) {
                    setUpScreenUiForFragment(MobileRechargeFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is ContactUsFragment) {
                    setUpScreenUiForFragment(ContactUsFragment.CLASS_NAME)
                } else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is KycFragment) {
                    setUpScreenUiForFragment(KycFragment.CLASS_NAME)
                }

                else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is AddMoneyEnterAmountFragment) {
                    setUpScreenUiForFragment(AddMoneyEnterAmountFragment.CLASS_NAME)
                }
                else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is NotificationFragment) {
                    setUpScreenUiForFragment(NotificationFragment.CLASS_NAME)
                }

                /*else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is GuulexFragment) {
                    setUpScreenUiForFragment(GuulexFragment.CLASS_NAME)
                }*/
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun initResources() {
        appData = AppData(this@HomeActivity, Constants.Keys._KeyCryptoPreference)
        Utils.hideKeyboard(this)
        Utils.hideKeyboardOnOutsideTouch(flFragmentContainerHome, this)
        tvCommonToolbar.setCustomFont(this, getString(R.string.quicksandMedium), true)
        parseIntent(intent)
    }

    override fun initListeners() {
        imgvwCommonToolbar.setOnClickListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.home_activity
    }

    override fun onClick(v: View?) {
        if (v == imgvwCommonToolbar) {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (appData!!.currency_symbol.isEmpty()) {
            FetchCountryCodesAsynTask().execute()
        }
    }

    /**
     * This method set the Activity UI according to the fragment, for example it changes toolbar icon's visibility according to fragment loaded
     */
    open fun setUpScreenUiForFragment(fragName: String) {
        imgvwGuulpayLogo.visibility = View.GONE
        imgvwCommonToolbar.visibility = View.VISIBLE
        tvCommonToolbar.visibility = View.VISIBLE
        Log.d("FragmentName", fragName)

        when (fragName) {

            ViewAllRecentTransFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.recentTransactions))
            }
            TermsAndConditionsFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.termsAndConditions))
            }
            EnterMobileForgotPasscodeFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.forgotPwd))
            }
            ChangePasscodeFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.changePasscode))
            }
            AddMoneyEnterAmountFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.add))
            }
            AddMoneyPaymentStatusFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.VISIBLE
                imgvwCommonToolbar.visibility = View.GONE
                tvCommonToolbar.visibility = View.GONE
            }
            SendMoneyPaymentStatus.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.VISIBLE
                imgvwCommonToolbar.visibility = View.GONE
                tvCommonToolbar.visibility = View.GONE
            }
            CompleteYourPaymentFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.completeYourPayment))
            }
            NotificationFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.notification))
            }
            ViewProfileFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.profile))
            }
            EditProfileFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.editProfile))
            }
            AddReceiverAccountFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.addReceiver))
            }
            RecieversFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.receivers))
            }
            AutoLoadAmountFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.autoLoad2))
            }
            AddBankAccountFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.addBankAccount))
            }
            EnterOtpFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.verifyOtp))
            }
            SavedBanksFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.savedBanks))
            }
            SavedCardsFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.savedCards))
            }
            MobileRechargeFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.recharge))
            }
            MobileRechargeHistFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.recent_recharges))
            }
            ContactUsFragment.CLASS_NAME -> {
                tvCommonToolbar.setText(getString(R.string.contact_us))
            }
            KycFragment.CLASS_NAME -> {
                tvCommonToolbar.text = "KYC Verification"
            }
            /*GuulexFragment.CLASS_NAME -> {
                tvCommonToolbar.text = "Guulex"
            }*/
        }

        /*else if (supportFragmentManager.findFragmentById(R.id.flFragmentContainerHome) is KycFragment) {
            setUpScreenUiForFragment(KycFragment.CLASS_NAME)
        }*/
    }

    /**
     * Parse intent value received either in onCreate and load the required fragment
     */
    fun parseIntent(intent: Intent?) {
        if (null != intent) {
            if (intent.hasExtra(Constants.Keys.KEY_FRAGMENT_NAME)) {
                val fragName = intent.getStringExtra(Constants.Keys.KEY_FRAGMENT_NAME)
                setUpScreenUiForFragment(fragName)
                when (fragName) {
                    ViewAllRecentTransFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ViewAllRecentTransFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, ViewAllRecentTransFragment.CLASS_NAME)
                    }
                    TermsAndConditionsFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, TermsAndConditionsFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, TermsAndConditionsFragment.CLASS_NAME)
                    }
                    EnterMobileForgotPasscodeFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, EnterMobileForgotPasscodeFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, EnterMobileForgotPasscodeFragment.CLASS_NAME)
                    }
                    ChangePasscodeFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ChangePasscodeFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, ChangePasscodeFragment.CLASS_NAME)
                    }
                    AddMoneyEnterAmountFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AddMoneyEnterAmountFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, AddMoneyEnterAmountFragment.CLASS_NAME)
                    }

                    NotificationFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, NotificationFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, NotificationFragment.CLASS_NAME)
                    }
                    ViewProfileFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ViewProfileFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, ViewProfileFragment.CLASS_NAME)
                    }
                    AddReceiverAccountFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AddReceiverAccountFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, AddReceiverAccountFragment.CLASS_NAME)
                    }
                    RecieversFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, RecieversFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, RecieversFragment.CLASS_NAME)
                    }
                    AutoLoadAmountFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AutoLoadAmountFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, AutoLoadAmountFragment.CLASS_NAME)
                    }
                    AddBankAccountFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AddBankAccountFragment.newInstance(""),
                                R.id.flFragmentContainerHome, false, AddBankAccountFragment.CLASS_NAME)
                    }
                    EnterOtpFragment.CLASS_NAME -> {
                        val mobile = intent.getStringExtra(Constants.Keys.KEY_MOBILE_NUMBER)
                        val fragName = intent.getStringExtra(Constants.Keys.KEY_FROM_WHICH_FRAGMENT_CALLING)
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, EnterOtpFragment.newInstance(fragName, mobile),
                                R.id.flFragmentContainerHome, false, EnterOtpFragment.CLASS_NAME)
                    }
                    SavedBanksFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, SavedBanksFragment.newInstance(""),
                                R.id.flFragmentContainerHome, false, SavedBanksFragment.CLASS_NAME)
                    }
                    SavedCardsFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, SavedCardsFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, SavedCardsFragment.CLASS_NAME)
                    }
                    SendMoneyPaymentStatus.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, SendMoneyPaymentStatus.newInstance(),
                                R.id.flFragmentContainerHome, false, SendMoneyPaymentStatus.CLASS_NAME)
                    }
                    MobileRechargeFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, MobileRechargeFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, MobileRechargeFragment.CLASS_NAME)
                    }
                    ContactUsFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ContactUsFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, ContactUsFragment.CLASS_NAME)
                    }
                    GuulexFragment.CLASS_NAME -> {
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexFragment.newInstance(),
                                R.id.flFragmentContainerHome, false, GuulexFragment.CLASS_NAME)
                    }
                }
            }
        }
    }


    private fun loadJSONFromAsset(): String? {
        var json: String? = ""
        try {
            val inputStream = getAssets().open(resources.getString(R.string.currenciesPath))
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            //showSomeErrorOccurredMsg(ex.toString())
        }

        return json
    }


    inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            val listType = object : TypeToken<ArrayList<CurrenciesModel>>() {}.type
            currenciesModellist = GsonBuilder().create().fromJson(loadJSONFromAsset(), listType)
            currenciesModellist =
                    return ""
        }

        override fun onPostExecute(result: String?) {
            if (currenciesModellist.size > 0) {
                for (i in currenciesModellist.indices) {
                    if (currenciesModellist.get(i).cc.equals(appData!!.register_currency_code)) {
                        appData!!.currency_symbol = currenciesModellist.get(i).symbol.toString()
                        // tvWalletAmt.text = appData!!.currency_symbol + " " + appData!!.register_amount
                        Log.d("Currency", appData!!.currency_symbol)
                        break
                    }
                }
            }
            loader.hide()
        }

    }

    override fun globalLogout() {
        appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}