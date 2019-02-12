package com.guulpay.myProfile.view

import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.others.AppData
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface ViewProfileContract {

    interface View : BaseView<Presenter> {
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        fun showUserDetails(userDetailsResponse: UserDetailsResponse.User?)
        fun showToast(msg: String)
    }

    interface Presenter : BasePresenter {
        fun showUserDetails()
        fun callUserDetailsApi(appData: AppData)
        fun callUSerEmailValidate(appData: AppData, email: String)

    }
}