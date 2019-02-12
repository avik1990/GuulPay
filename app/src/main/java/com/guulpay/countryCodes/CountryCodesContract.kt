package com.guulpay.countryCodes

import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface CountryCodesContract {

    interface View: BaseView<Presenter> {
        fun countryCodesFetched(list:ArrayList<CountryCodesModel.Prefixlist>)
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun fetchCountryCodes()
    }
}