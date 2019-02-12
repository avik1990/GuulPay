package com.guulpay.myProfile.edit

import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.others.AppData
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView
import okhttp3.MultipartBody

interface EditProfileContract {

    interface View : BaseView<Presenter> {
        fun selectedGender(gender: String)
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        fun showUserDetails(userDetailsResponse: UserDetailsResponse.User?)
        fun fieldsValidationFailed(msg: String)
        fun enableButton()
        fun disableButton()
        fun showUserImage(img_url: String, msg: String)
    }

    interface Presenter : BasePresenter {
        fun selectGender()
        fun saveProfile()
        fun showUserDetails()
        fun callUserDetailsApi(appData: AppData)
        fun checkFieldsValidation(name: String, addressline1: String, city: String, state: String, zipcode: String)
        fun callEditProfileApi(appData: AppData, name: String, addressline1: String, city: String, state: String, zipcode: String, dob: String, gender: String, addressline2: String)
        fun UploadProfileImage(imagefile: MultipartBody.Part, user_id: String)
    }
}