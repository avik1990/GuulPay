package com.guulpay.addMoney

import com.guulpay.others.AppData
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface AddMoneyContract {

    interface AddAmountContract {
        interface View : BaseView<Presenter> {
            fun calculatedAmt(addAmt: String)
            fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        }

        interface Presenter : BasePresenter {
            fun addMoney()
            fun addAmt100(amt: Float)
            fun addAmt500(amt: Float)
            fun addAmt1000(amt: Float)
            fun clearAmt()
            fun callWalletRecharegApi(amt: String, appData: AppData)
        }
    }

    interface CompleteYourPaymentContract {
        interface View : BaseView<Presenter> {
            fun expandDebitCardLayout()
            fun collapseDebitCardLayout()
            fun expandCreditCardLayout()
            fun collapseCreditCardLayout()
            fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        }

        interface Presenter : BasePresenter {
            fun payNow()
            fun onDebitCardLayoutClick()
            fun onCreditCardLayoutClick()
        }
    }
}