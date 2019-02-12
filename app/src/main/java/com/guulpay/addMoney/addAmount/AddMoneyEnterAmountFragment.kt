package com.guulpay.addMoney.addAmount

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addMoney.AddMoneyContract
import com.guulpay.addMoney.addAmount.servicecall.AddMoneyRepositoryProvider
import com.guulpay.customUiComponents.EnterAmountEditTextHandler
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.add_money_fragment.*
import kotlinx.android.synthetic.main.money_amount_layout.*

class AddMoneyEnterAmountFragment : BaseFragment(), View.OnClickListener, AddMoneyContract.AddAmountContract.View {
    override fun globalLogout() {
    }

    lateinit var addMoneyPresenter: AddMoneyEnterAmountPresenter
    var amount = 0.00F
    var appData: AppData? = null

    companion object {
        const val CLASS_NAME = "AddMoneyEnterAmountFragment"
        fun newInstance(): Fragment {
            return AddMoneyEnterAmountFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.add_money_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        AddMoneyEnterAmountPresenter(this, appData!!, AddMoneyRepositoryProvider.calladdmoneyAPI(appData!!.remember_token)).start()
        EnterAmountEditTextHandler(etMoney, object : EnterAmountEditTextHandler.EnterAmountEditTextListener {
            override fun isFieldBlank(fieldIsBlank: Boolean, amt: String) {

                if (fieldIsBlank) {
                    amount = 0.00F
                    btAddMoney.alpha = 0.5F
                    btAddMoney.isEnabled = false
                    addMoneyPresenter.clearAmt()
                } else {
                    //amount = amt.toFloat()
                    btAddMoney.alpha = 1.0F
                    btAddMoney.isEnabled = true
                }
            }
        })

        //  getCurrencySymbol("INR")
    }

    /*  fun getCurrencySymbol(currencyCode: String): String {
          try {
              val currency = Currency.getInstance(currencyCode)
              return currency.symbol
          } catch (e: Exception) {
              return currencyCode
          }

      }*/

    override fun initListeners() {
        btAddMoney.setOnClickListener(this)
        btAmt100.setOnClickListener(this)
        btAmt500.setOnClickListener(this)
        btAmt1000.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btAmt100) {
            btAmt100.text = resources.getString(R.string.bt100Select)
            btAmt500.text = resources.getString(R.string.bt500Deselect)
            btAmt1000.text = resources.getString(R.string.bt1000Deselect)
            addMoneyPresenter.addAmt100(amount)
        } else if (v == btAmt500) {
            btAmt500.text = resources.getString(R.string.bt500Select)
            btAmt1000.text = resources.getString(R.string.bt1000Deselect)
            btAmt100.text = resources.getString(R.string.bt100Deselect)
            addMoneyPresenter.addAmt500(amount)
        } else if (v == btAmt1000) {
            btAmt1000.text = resources.getString(R.string.bt1000Select)
            btAmt500.text = resources.getString(R.string.bt500Deselect)
            btAmt100.text = resources.getString(R.string.bt100Deselect)
            addMoneyPresenter.addAmt1000(amount)
        } else if (v == btAddMoney) {
            if (!etMoney.text.toString().trim().isNullOrBlank()) {
                var amt = etMoney.text.toString().trim().toFloat()
                if (amt > 0) {
                    addMoneyPresenter.callWalletRecharegApi(etMoney.text.toString().trim(), appData!!)
                } else {
                    showSomeErrorOccurredMsg("Amount must be greater than zero")
                }
            } else {
                showSomeErrorOccurredMsg("Enter Amount to Recharge")
            }
        }
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        activity?.onBackPressed()
        Utils.showToast(context, "Transaction Successful.")
        /*(activity as HomeActivity).setUpScreenUiForFragment(CompleteYourPaymentFragment.CLASS_NAME)
        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, CompleteYourPaymentFragment.newInstance(),
                R.id.flFragmentContainerHome, true, CompleteYourPaymentFragment.CLASS_NAME)*/
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: AddMoneyContract.AddAmountContract.Presenter) {
        addMoneyPresenter = presenter as AddMoneyEnterAmountPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(rlAddMoneyParent, getString(R.string.networkUnavailable), 3000)

    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(rlAddMoneyParent, msg, 3000)
    }

    override fun calculatedAmt(addAmt: String) {
        //etMoney.setText("$"+addAmt)
        etMoney.setText(addAmt)
        etMoney.setSelection(etMoney.text.length) // To make the cursor blink at the end of text
    }
}