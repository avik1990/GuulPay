package com.guulpay.countryWiseBanks

import com.guulpay.countryCodes.models.CountryWiseBankModel
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface CountryWiseBanksContract {

    interface View: BaseView<Presenter> {
        fun countryBankcsFetched(list:ArrayList<CountryWiseBankModel>)
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun fetchcountryBanks()
    }
}