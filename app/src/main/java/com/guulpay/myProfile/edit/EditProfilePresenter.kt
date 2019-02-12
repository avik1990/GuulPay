package com.guulpay.myProfile.edit

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.myProfile.model.UserImageUploadResponse
import com.guulpay.myProfile.servicecall.MyProfileRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import org.json.JSONObject


class EditProfilePresenter(private val context: EditProfileFragment, private val editProfView: EditProfileContract.View, private val myProfileRepository: MyProfileRepository, private val appData: AppData) : EditProfileContract.Presenter {


    val TAG = "EditProfilePresenter"
    private var disposable: Disposable? = null
    var selectedGender: Int = 0

    override fun selectGender() {
        selectedGender = selectedGender + 1
        if (selectedGender == 0) {
            editProfView.selectedGender(context.getString(R.string.male))
        } else if (selectedGender == 1) {
            editProfView.selectedGender(context.getString(R.string.female))
        } else if (selectedGender == 2) {
            editProfView.selectedGender(context.getString(R.string.transgender))
        } else if (selectedGender == 3) {
            selectedGender = -1
            editProfView.selectedGender(context.getString(R.string.others))
        }
    }

    override fun saveProfile() {

    }

    override fun start() {
        editProfView.setPresenter(this)
    }

    override fun stop() {

    }

    override fun showUserDetails() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callUserDetailsApi(appData: AppData) {
        if (!editProfView.isNetworkAvailable()) {
            editProfView.showNetworkUnavailableMsg()
            return
        }
        editProfView.handleProgressAlert(true)
        disposable = myProfileRepository.callUserDetailsApi(appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    editProfView.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.e(TAG, json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            editProfView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val userDetailsResponse = GsonBuilder().create().fromJson(json, UserDetailsResponse::class.java)
                            Log.d("signupresponsesucess", userDetailsResponse.toString())
                            editProfView.showUserDetails(userDetailsResponse.user)
                            if (userDetailsResponse.user!!.gender.equals("M")) {
                                selectedGender = -1
                            } else if (userDetailsResponse.user!!.gender.equals("F")) {
                                selectedGender = 0
                            } else if (userDetailsResponse.user!!.gender.equals("T")) {
                                selectedGender = 1
                            } else if (userDetailsResponse.user!!.gender.equals("O")) {
                                selectedGender = 2
                            }

                            if (!userDetailsResponse.user!!.gender!!.isEmpty()) {
                                selectGender()
                            }

                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (editProfView.isActivityRunning()) {
                        editProfView.handleProgressAlert(false)
                        if (editProfView.isNetworkAvailable())
                            editProfView.showSomeErrorOccurredMsg("Something went wrong")
                        else editProfView.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun checkFieldsValidation(name: String, addressline1: String, city: String, state: String, zipcode: String) {
        editProfView.disableButton()
        if (name.trim().length == 0) {
            editProfView.fieldsValidationFailed("Please enter name")
            return
        }

        if (addressline1.trim().length == 0) {
            editProfView.fieldsValidationFailed("Please enter address")
            return
        }

        if (city.trim().length == 0) {
            editProfView.fieldsValidationFailed("Please enter City")
            return
        }

        if (state.trim().length == 0) {
            editProfView.fieldsValidationFailed("Please enter State")
            return
        }

        if (zipcode.trim().length == 0) {
            editProfView.fieldsValidationFailed("Please enter Zip Code")
            return
        }

        editProfView.enableButton()

    }

    override fun callEditProfileApi(appData: AppData, name: String, addressline1: String, city: String, state: String, zipcode: String, dob: String, gender: String, addressline2: String) {
        if (!editProfView.isNetworkAvailable()) {
            editProfView.showNetworkUnavailableMsg()
            return
        }

        editProfView.handleProgressAlert(true)
        disposable = myProfileRepository.callEditProfileApi(appData, name, addressline1, city, state, zipcode, dob, gender, addressline2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    editProfView.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.d("response", json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            editProfView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            editProfView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            editProfView.goToNextPage()
                        }else if (obj.get("responseCode").toString().equals("701")) {
                            editProfView.globalLogout()
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (editProfView.isActivityRunning()) {
                        editProfView.handleProgressAlert(false)
                        if (editProfView.isNetworkAvailable())
                            editProfView.showSomeErrorOccurredMsg("Something went wrong")
                        else editProfView.showNetworkUnavailableMsg()
                    }
                })
    }

    override fun UploadProfileImage(imagefile: MultipartBody.Part, user_id: String) {
        if (!editProfView.isNetworkAvailable()) {
            editProfView.showNetworkUnavailableMsg()
            return
        }
        editProfView.handleProgressAlert(true)
        disposable = myProfileRepository.callProfileImageApi(imagefile, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    editProfView.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            editProfView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            //Log.d("Response", json)
                            val userImageUploadResponse = GsonBuilder().create().fromJson(json, UserImageUploadResponse::class.java)
                            appData.profile_image = userImageUploadResponse.originalImage.toString()
                            editProfView.showUserImage(appData.profile_image, userImageUploadResponse.responseDetails.toString())

                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (editProfView.isActivityRunning()) {
                        editProfView.handleProgressAlert(false)
                        if (editProfView.isNetworkAvailable())
                            editProfView.showSomeErrorOccurredMsg("Something went wrong")
                        else editProfView.showNetworkUnavailableMsg()
                    }
                })
    }


}