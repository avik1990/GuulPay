package com.guulpay.addBankAccount


class AddBankAccountPresenter(private val context: AddBankAccountFragment, private val addbankaccount: AddBankContract.View) : AddBankContract.Presenter {
    override fun checkFieldsValidation(name: String, mobile: String, email: String, passcode: String, confirmPasscode: String, isChecked: Boolean) {
    }


    override fun start() {
        addbankaccount.setPresenter(this)
    }

    override fun stop() {

    }
}