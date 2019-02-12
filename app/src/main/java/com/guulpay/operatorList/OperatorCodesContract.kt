package com.guulpay.operatorList

import com.guulpay.operatorList.models.OperatorModel
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface OperatorCodesContract {

    interface View: BaseView<Presenter> {
        fun operatorFetched(list:ArrayList<OperatorModel.Operatorlist>)
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun fetchOperators()
    }
}