package com.guulpay.guulexgraph

import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface GuulexGraphContract {

    interface View : BaseView<Presenter> {
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        fun fieldsValidationFailed(msg: String)
        fun disableButton()
        fun enableButton()
    }

    interface Presenter : BasePresenter {
        fun callContactUsAPi(name: String, email: String, message: String)
    }
}