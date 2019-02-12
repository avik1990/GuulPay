package com.guulpay.notification

import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface NotificationContract {

    interface View : BaseView<Presenter> {
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        fun fieldsValidationFailed(msg: String)
        fun disableButton()
        fun enableButton()

    }

    interface Presenter : BasePresenter {
       // fun callContactUsAPi(name: String, email: String, message: String)
     //   fun checkFieldsValidation(name: String, email: String, message: String)
    }
}