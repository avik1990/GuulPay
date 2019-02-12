package com.guulpay.myProfile.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.kycverification.model.KYCRequestVerificationModel
import com.guulpay.myProfile.model.*
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable
import okhttp3.MultipartBody

class MyProfileRepository(private val apiService: APIService) {

    fun callUserDetailsApi(appData: AppData): Observable<BaseResponse> {
        var userProfileRequest = UserProfileRequest()
        var aboutUsRequest_request = UserProfileRequest().RequestVariables()
        var signUpRequest_data = UserProfileRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.user_id = appData!!.user_id

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        // return apiService.callAboutusAPI(ObjectToJSonConvertor.getRequestJson(aboutUsRequest))
        return apiService.callProfiledetailsPostAPI(userProfileRequest)
    }

    fun callEditProfileApi(appData: AppData, name: String, addressline1: String, city: String, state: String, zipcode: String, dob: String, gender: String, addressline2: String): Observable<BaseResponse> {
        var userProfileRequest = UserEditRequest()
        var aboutUsRequest_request = UserEditRequest().RequestVariables()
        var signUpRequest_data = UserEditRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.userId = appData!!.user_id
        aboutUsRequest_request.name = name
        aboutUsRequest_request.addressLine1 = addressline1
        aboutUsRequest_request.addressLine2 = addressline2
        aboutUsRequest_request.city = city
        aboutUsRequest_request.state = state
        aboutUsRequest_request.zipcode = zipcode
        aboutUsRequest_request.dob = dob
        aboutUsRequest_request.gender = gender

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data

        Log.d("UserEditRequest", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        // return apiService.callAboutusAPI(ObjectToJSonConvertor.getRequestJson(aboutUsRequest))
        return apiService.callEditProfilePostAPI(userProfileRequest)
    }


    fun callProfileImageApi(image: MultipartBody.Part, user_id: String): Observable<BaseResponse> {
        return apiService.callProfileImagePostAPI(image, user_id)
    }

    fun callVarifyEmailApi(appData: AppData): Observable<BaseResponse> {
        var userProfileRequest = UserEmailVerificationRequest()
        var aboutUsRequest_request = UserEmailVerificationRequest().RequestVariables()
        var signUpRequest_data = UserEmailVerificationRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.email = appData!!.register_email

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        return apiService.callVerifyEmailPostAPI(userProfileRequest)
    }


    fun callGenerateUrlApi(signicate_method: String, appData: AppData): Observable<BaseResponse> {
        var userProfileRequest = KYCRequestVerificationModel()
        var aboutUsRequest_request = KYCRequestVerificationModel().RequestVariables()
        var signUpRequest_data = KYCRequestVerificationModel().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.email = appData!!.register_email
        aboutUsRequest_request.device_type = "andorid"
        aboutUsRequest_request.signicate_method = signicate_method
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data
        Log.d("View_profile_data", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        return apiService.callKYCURLGeneratorPostAPI(userProfileRequest)
    }

    fun callKYCMethodCgheckApi(appData: AppData): Observable<BaseResponse> {
        var userProfileRequest = KYCMethodsCheckRequest()
        var aboutUsRequest_request = KYCMethodsCheckRequest().RequestVariables()
        var signUpRequest_data = KYCMethodsCheckRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.userId = appData!!.user_id
        aboutUsRequest_request.country_id = appData!!.register_country_code
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        return apiService.callKYCURLMethodPostAPI(userProfileRequest)
    }

    fun callKYCMethodinViewProfile(appData: AppData): Observable<BaseResponse> {
        var userProfileRequest = KYCMethodsCheckRequest()
        var aboutUsRequest_request = KYCMethodsCheckRequest().RequestVariables()
        var signUpRequest_data = KYCMethodsCheckRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.userId = appData!!.user_id
        aboutUsRequest_request.country_id = appData!!.register_country_code

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        userProfileRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(userProfileRequest).toString())
        return apiService.callKYCURLMethodPostAPI(userProfileRequest)
    }


    fun callUSerEmailValidate(appData: AppData, email: String): Observable<BaseResponse> {

        var userEmailVerify = UserEmailVerify()
        var userEmail_data_request = UserEmailVerify().RequestVariables()
        var emailVerifyData = UserEmailVerify().Request()
        var randomkey = Utils.generateRandom16bitKey()

        var encryptedstring: String
        userEmail_data_request.email = appData.register_email
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(userEmail_data_request, randomkey)
        emailVerifyData.data = encryptedstring
        emailVerifyData.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        Log.v("email_salt", emailVerifyData.salt)
        userEmailVerify.request = emailVerifyData
        Log.v("UserEmailRequest", userEmailVerify.request.toString())
        return apiService.callUserEmailValidate(userEmailVerify)
    }


}