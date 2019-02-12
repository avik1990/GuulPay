package com.guulpay.guulex

import com.guulpay.guulex.model.ForexResponse
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface GuulexContract {

    interface View : BaseView<Presenter> {
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        fun fieldsValidationFailed(msg: String)
        fun inflateData(list: ArrayList<ForexResponse.Forexlist>)
    }

    interface Presenter : BasePresenter {
        fun getForexData()
    }
}