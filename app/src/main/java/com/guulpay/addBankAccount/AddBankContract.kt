package com.guulpay.addBankAccount

import android.content.Context
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface AddBankContract {

    interface View: BaseView<Presenter> {
        fun getContext(): Context
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun checkFieldsValidation(name:String, mobile:String, email:String, passcode:String,
                                  confirmPasscode:String, isChecked:Boolean)
    }
}