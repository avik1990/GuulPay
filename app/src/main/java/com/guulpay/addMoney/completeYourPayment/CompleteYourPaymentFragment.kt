package com.guulpay.addMoney.completeYourPayment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addBankAccount.AddBankAccountFragment
import com.guulpay.addMoney.AddMoneyContract
import com.guulpay.addMoney.passcode.AddMoneyPasscodeFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.complete_your_payment.*

class CompleteYourPaymentFragment : BaseFragment(), View.OnClickListener, AddMoneyContract.CompleteYourPaymentContract.View {
    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var completeYourPaymentPresenter: CompleteYourPaymentPresenter
    lateinit var savedCardsAdapter: SavedCardsAddmoneyAdapter

    companion object {
        const val CLASS_NAME = "CompleteYourPaymentFragment"
        fun newInstance(): Fragment {
            return CompleteYourPaymentFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.complete_your_payment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        CompleteYourPaymentPresenter(this).start()

        savedCardsAdapter = SavedCardsAddmoneyAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewSavedCards.layoutManager = mLayoutManager
        recyclerViewSavedCards.itemAnimator = DefaultItemAnimator()
        recyclerViewSavedCards.adapter = savedCardsAdapter
    }

    override fun initListeners() {
        tvPayNowFromDebitCrd.setOnClickListener(this)
        tvPayNowFromCreditCrd.setOnClickListener(this)
        tvPayNowBankDet.setOnClickListener(this)
        tvAddNewBank.setOnClickListener(this)
        tvDebitCard.setOnClickListener(this)
        tvCreditCard.setOnClickListener(this)

        chckbx.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                recyclerViewSavedCards.visibility = View.GONE
                tvSavedCardsText.visibility = View.GONE
                tvPayNowBankDet.visibility = View.VISIBLE
                //   tvAddNewBank.visibility = View.VISIBLE
            } else {
                recyclerViewSavedCards.visibility = View.VISIBLE
                tvSavedCardsText.visibility = View.VISIBLE
                tvPayNowBankDet.visibility = View.GONE
                //   tvAddNewBank.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == tvDebitCard) {
            completeYourPaymentPresenter.onDebitCardLayoutClick()
        } else if (v == tvCreditCard) {
            completeYourPaymentPresenter.onCreditCardLayoutClick()
        } else if (v == tvAddNewBank) {
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddBankAccountFragment.newInstance(CLASS_NAME),
                    R.id.flFragmentContainerHome, false, AddBankAccountFragment.CLASS_NAME)
        } else if (v == tvPayNowBankDet) {
            (activity as HomeActivity).setUpScreenUiForFragment(CompleteYourPaymentFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddMoneyPasscodeFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, AddMoneyPasscodeFragment.CLASS_NAME)
        } else if (v == tvPayNowFromDebitCrd) {
            tvPayNowBankDet.performClick()
        } else if (v == tvPayNowFromCreditCrd) {
            tvPayNowBankDet.performClick()
        }
    }

    /* Callback methods of AddMoneyContract.CompleteYourPaymentContract.View */

    override fun expandDebitCardLayout() {
        tvDebitCard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0)
        include_debitcard_layout.visibility = View.VISIBLE
        llChckbxSavedDebitCard.visibility = View.VISIBLE
        tvPayNowFromDebitCrd.visibility = View.VISIBLE
        collapseCreditCardLayout()
    }

    override fun collapseDebitCardLayout() {
        tvDebitCard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0)
        include_debitcard_layout.visibility = View.GONE
        llChckbxSavedDebitCard.visibility = View.GONE
        tvPayNowFromDebitCrd.visibility = View.GONE
    }

    override fun expandCreditCardLayout() {
        tvCreditCard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0)
        include_creditcard_layout.visibility = View.VISIBLE
        llChckbxSavedCreditCard.visibility = View.VISIBLE
        tvPayNowFromCreditCrd.visibility = View.VISIBLE
        collapseDebitCardLayout()
    }

    override fun collapseCreditCardLayout() {
        tvCreditCard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0)
        include_creditcard_layout.visibility = View.GONE
        llChckbxSavedCreditCard.visibility = View.GONE
        tvPayNowFromCreditCrd.visibility = View.GONE
    }

    override fun handleProgressAlert(showingStatus: Boolean) {

    }

    override fun goToNextPage() {

    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: AddMoneyContract.CompleteYourPaymentContract.Presenter) {
        completeYourPaymentPresenter = presenter as CompleteYourPaymentPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {

    }

    override fun showSomeErrorOccurredMsg(msg: String) {

    }
}