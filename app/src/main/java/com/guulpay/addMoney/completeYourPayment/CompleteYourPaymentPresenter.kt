package com.guulpay.addMoney.completeYourPayment

import com.guulpay.addMoney.AddMoneyContract


class CompleteYourPaymentPresenter(private val view: AddMoneyContract.CompleteYourPaymentContract.View) : AddMoneyContract.CompleteYourPaymentContract.Presenter {

    var isDebitCardLayoutVisible:Boolean = false
    var isCreditCardLayoutVisible:Boolean = false

    override fun payNow() {
        view.goToNextPage()
    }

    override fun onDebitCardLayoutClick() {
        if (isDebitCardLayoutVisible){
            view.collapseDebitCardLayout()
        }
        else{
            view.expandDebitCardLayout()
        }
        isDebitCardLayoutVisible = !isDebitCardLayoutVisible
    }

    override fun onCreditCardLayoutClick() {
        if (isCreditCardLayoutVisible){
            view.collapseCreditCardLayout()
        }
        else{
            view.expandCreditCardLayout()
        }
        isCreditCardLayoutVisible = !isCreditCardLayoutVisible
    }

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {

    }
}