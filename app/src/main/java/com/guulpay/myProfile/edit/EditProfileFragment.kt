package com.guulpay.myProfile.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.changePasscode.model.ChangePasscodeResponseModel
import com.guulpay.changePasscode.servicecall.ChangepasscodeRepository
import com.guulpay.customUiComponents.CommonDialog
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.fragments.BaseFragment
import com.guulpay.kycverification.KYCActivity
import com.guulpay.kycverification.KycFragment
import com.guulpay.kycverification.model.KYCUrlGeneratorResponse
import com.guulpay.login.LoginActivity
import com.guulpay.myProfile.model.KycVerificationResponse
import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.myProfile.servicecall.MyProfileRepository
import com.guulpay.myProfile.servicecall.MyProfileRepositoryProvider
import com.guulpay.others.*
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.others.encryption.ObjectToJSonConvertor
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.change_passcode.*
import kotlinx.android.synthetic.main.myprofile_edit_fragment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder

class EditProfileFragment : BaseFragment(), View.OnClickListener, EditProfileContract.View, DatePickerDialog.OnDateSetListener, CommonDialog.CommonDialogClickListener {

    var appData: AppData? = null
    lateinit var editProfilePresenter: EditProfilePresenter
    lateinit var animLeftIn: Animation
    lateinit var animRightOut: Animation
    var isTyping = false
    var myProfileRepository: MyProfileRepository? = null

    var date: String? = null
    var gender_value: String = ""
    var url: String = ""
    var encryptedstring: String? = ""
    var saltkey: String? = ""
    var loadurl: String? = ""
    var internalloadurl: String? = ""
    private var disposable: Disposable? = null
    var isBank_: Boolean = false
    var isPassport_: Boolean = false
    var isDriving_: Boolean = false
    var isNational_: Boolean = false

    companion object {
        const val CLASS_NAME = "EditProfileFragment"
        fun newInstance(): Fragment {
            return EditProfileFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.myprofile_edit_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        myProfileRepository = MyProfileRepositoryProvider.getUserDetsils(appData!!.remember_token)
        //val textSignUpUpper = getString(R.string.dontHaveAnAccount) + " <b><font color=#348C96> " + getString(R.string.signUp) + " </font></b>" + " here"
        (activity as HomeActivity).setUpScreenUiForFragment(CLASS_NAME)
        EditProfilePresenter(this@EditProfileFragment, this, myProfileRepository!!, appData!!).start()
        editProfilePresenter?.callUserDetailsApi(appData!!)
        animLeftIn = AnimationUtils.loadAnimation(context, R.anim.enter_from_left)
        animRightOut = AnimationUtils.loadAnimation(context, R.anim.exit_to_right)


        CustomTextWatcher(etNameProfileEdit, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                editProfilePresenter.checkFieldsValidation(etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString())
            }
        })
        CustomTextWatcher(etAddressLine1, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                editProfilePresenter.checkFieldsValidation(etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString())
            }
        })
        CustomTextWatcher(etCity, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                editProfilePresenter.checkFieldsValidation(etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString())
            }
        })
        CustomTextWatcher(etState, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                editProfilePresenter.checkFieldsValidation(etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString())
            }
        })
        CustomTextWatcher(etZipCode, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                editProfilePresenter.checkFieldsValidation(etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString())
            }
        })
    }

    override fun enableButton() {
        btnSave.isEnabled = true
        btnSave.alpha = 1.0F
    }

    override fun disableButton() {
        btnSave.isEnabled = false
        btnSave.alpha = 0.5F
    }

    override fun initListeners() {
      //  tvKycVerification.setOnClickListener(this)
        tvDob.setOnClickListener(this)
        imgvwEditPicture.setOnClickListener(this)
        tvGenderProfileEdit.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == tvGenderProfileEdit) {
            tvGenderProfileEdit.startAnimation(animRightOut)
            editProfilePresenter.selectGender()

        } else if (v == imgvwEditPicture) {
            CropImage.activity()
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAllowCounterRotation(true)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .start(context as Context, this)
        } else if (v == tvDob) {
            showDatePickerDialog(1980, 0, 1, R.style.DatePickerSpinner)
        } else if (v == btnSave) {
            if (!date!!.isEmpty()) {
                if (tvGenderProfileEdit.text.toString().equals("Male")) {
                    gender_value = "M"
                } else if (tvGenderProfileEdit.text.toString().equals("Female")) {
                    gender_value = "F"
                } else if (tvGenderProfileEdit.text.toString().equals("Transgender")) {
                    gender_value = "T"
                } else if (tvGenderProfileEdit.text.toString().equals("Others")) {
                    gender_value = "O"
                }
                editProfilePresenter.callEditProfileApi(appData!!, etNameProfileEdit.text.toString(), etAddressLine1.text.toString(), etCity.text.toString(), etState.text.toString(), etZipCode.text.toString(), date!!, gender_value, etAddressLine2.text.toString())
            } else {
                Utils.showSnackbar(llProfileedit, "Please select Birthday", 3000)
            }
        }
      /*  else if (v == tvKycVerification) {
            if (!isNetworkAvailable()) {
                showNetworkUnavailableMsg()
                return
            }
            checKYCMethods()
        }*/
    }

    private fun checKYCMethods() {
        loader.show()
        disposable = myProfileRepository!!.callKYCMethodCgheckApi(appData!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loader.hide()
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("Json_Stirng", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val kycVerificationResponse = GsonBuilder().create().fromJson(json, KycVerificationResponse::class.java)


                            if (kycVerificationResponse.methods!!.size > 0) {
                                //  passport","bank","national_id
                                if (kycVerificationResponse.methods.toString().contains("passport")) {
                                    isPassport_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("bank")) {
                                    isBank_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("national_id")) {
                                    isNational_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("drivers_license")) {
                                    isDriving_ = true
                                }

                                CommonDialog.getInstance("Choose a Verification Method", "", "Cancel", "Ok", false, this, isBank = isBank_, isDriving = isDriving_, isNational = isNational_, isPassport = isPassport_).show(activity?.fragmentManager, "")
                            }
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            globalLogout()
                        }
                    }
                }, {
                    if ((activity as HomeActivity).isActivityVisible) {
                        loader.hide()
                        Utils.showSnackbar(llProfileedit, "Something went wrong", 3000)
                    }
                })
    }


    override fun onRightClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightClick(pos: Int) {
        var randomkey = Utils.generateRandom16bitKey()
        saltkey = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")

        when (pos) {
            0 -> {
                /*encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&tupas", randomkey))
                loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                getSignigetUrl("0", "tupas")
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                //  Utils.openUrl(loadurl!!, context!!)

            }
            1 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-passport-selfie", randomkey))
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /* Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                         R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                // Utils.openUrl(loadurl!!, context!!)
                getSignigetUrl("1", "scid-passport-selfie")


            }
            2 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-dl-selfie", randomkey), "utf-8")
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/

                getSignigetUrl("2", "scid-dl-selfie")

                //Utils.openUrl(loadurl!!, context!!)
            }
            3 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-ic-selfie", randomkey))
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                getSignigetUrl("3", "scid-dl-selfie")

                //Utils.openUrl(loadurl!!, context!!)
            }
        }
    }

    fun showDatePickerDialog(year: Int, monthOfYear: Int, dayOfMonth: Int, spinnerTheme: Int) {
        SpinnerDatePickerDialogBuilder()
                .context(context)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .maxDate(Constants.Values.MAX_YEAR, Constants.Values.MAX_MONTH, Constants.Values.MAX_DAY)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show()
    }

    private var editedBmp: Bitmap? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                editProfilePresenter.UploadProfileImage(preparefilepart(resultUri), appData!!.user_id)
                Glide.with(this)
                        .asBitmap()
                        .load(resultUri)
                        .thumbnail(0.001f)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                editedBmp = resource.resize(400, 400)
                                /*val base64 = (editedBmp
                                        ?: resource) toBase64 Bitmap.CompressFormat.PNG
                                editProfilePic(base64)*/
                                ivProfileimg?.setImageBitmap(editedBmp)
                            }
                        })
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun showUserImage(img_url: String, msg: String) {
        if (!msg.isEmpty()) {
            Utils.showToast(context, msg)
        }
        try {
            Picasso.get().load(img_url)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(ivProfileimg)
        } catch (e: Exception) {

        }

    }

    private fun preparefilepart(uri: Uri): MultipartBody.Part {
        //val bin = File(uri.toString())
        val bin = FileUtils.getFile(context, Uri.parse(uri.toString()))
        val reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), bin)
        return MultipartBody.Part.createFormData("image", bin.name, reqBody)
    }

    /* DatePickerDialog.OnDateSetListener */
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        /* Month index starts from 0 */
        date = year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()
        //// tvDob.text = Utils.formatDate(dayOfMonth, monthOfYear + 1, year)
        tvDob.text = Utils.getFormasssttedDate(date!!)

    }

    /* Callback methods of EditProfileContract.View */
    override fun selectedGender(gender: String) {
        tvGenderProfileEdit.startAnimation(animLeftIn)
        tvGenderProfileEdit.text = gender
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        Utils.showToast(context, "User profile updated successfully.")
        activity!!.finish()
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: EditProfileContract.Presenter) {
        editProfilePresenter = presenter as EditProfilePresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llProfileedit, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llProfileedit, msg, 3000)
    }

    override fun showUserDetails(userDetailsResponse: UserDetailsResponse.User?) {
        etNameProfileEdit.setText(userDetailsResponse!!.name)
        etEmailEditProfile.setText(userDetailsResponse!!.email)
        etEmailEditProfile.isEnabled = false
        etAddressLine1.setText(userDetailsResponse!!.addressLine1)
        etAddressLine2.setText(userDetailsResponse!!.addressLine2)
        etCity.setText(userDetailsResponse!!.city)
        etState.setText(userDetailsResponse!!.state)
        etZipCode.setText(userDetailsResponse!!.zipcode)
        date = userDetailsResponse!!.dateOfBirth
        showUserImage(userDetailsResponse!!.mainimage.toString(), "")
        ///Log.d("appData.register", appData!!.register_currency_code)
        /*if (!userDetailsResponse!!.is_kyc_verified.equals("1") && appData!!.register_currency_code.contains("FI")) {
            tvKycVerification.visibility = View.VISIBLE
        } else {
            tvKycVerification.visibility = View.GONE
        }*/

        if (userDetailsResponse.is_kyc_verified.equals("1")) {


            //etNameProfileEdit.isEnabled = false
            //Changes made to edit the User Name After KYC Verified. Temporary Change
            etNameProfileEdit.isEnabled = true
            //tvKycVerification.visibility = View.VISIBLE
        } else {

            //Changes By Rishabh 5/02/2019

           // tvKycVerification.visibility = View.VISIBLE
        }

        try {
            tvDob.text = Utils.getFormasssttedDate(userDetailsResponse!!.dateOfBirth.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun fieldsValidationFailed(msg: String) {
        if (!isTyping)
            Utils.showSnackbar(llProfileedit, msg, 3000)
    }


    fun getSignigetUrl(index: String, signicate_method: String) {
        loader.show()
        disposable = myProfileRepository!!.callGenerateUrlApi(signicate_method, appData!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loader.hide()
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("Json_Stirng", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val kYCUrlGeneratorResponse = GsonBuilder().create().fromJson(json, KYCUrlGeneratorResponse::class.java)
                            redirecturl(index, kYCUrlGeneratorResponse.redirectUrl)
                            // Utils.showSnackbar(llProfileedit, "Success", 3000)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            //view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            (activity as HomeActivity).globalLogout()
                        }
                    }
                }, {
                    if ((activity as HomeActivity).isActivityVisible) {
                        loader.hide()
                        Utils.showSnackbar(llProfileedit, "Something went wrong", 3000)
                    }
                })
    }

    private fun redirecturl(index: String, redirectUrl: String?) {
        /*when {
            index.equals("0") -> Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(URLDecoder.decode(redirectUrl, "UTF-8")!!), R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)
            index.equals("1") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("2") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("3") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
        }*/

        when {
            index.equals("0") -> {
                val intent = Intent(context, KYCActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_URL, URLDecoder.decode(redirectUrl, "UTF-8"))
                startActivity(intent)
            }
            index.equals("1") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("2") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("3") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
        }

    }

    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(context, LoginActivity::class.java)
        activity!!.startActivity(intent)
        activity!!.finishAffinity()
    }
}