package com.guulpay.kycverification

import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface KycVerficationContract {

    interface View : BaseView<Presenter> {
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
       // fun setAboutusdata(htmlcontent: String)
    }

    interface Presenter : BasePresenter {
     //   fun callAboutUs()
    }
}