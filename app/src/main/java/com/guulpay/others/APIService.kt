package com.guulpay.others

import com.guulpay.aboutus.model.AboutUsRequest
import com.guulpay.addMoney.addAmount.model.AddMoneyWalletRequest
import com.guulpay.basemodel.BaseResponse
import com.guulpay.changePasscode.model.ChangePasscodeRequest
import com.guulpay.contactus.model.ContactUsRequest
import com.guulpay.enterOtp.model.EnterOTPRequest
import com.guulpay.enterOtp.model.ForceLoginRequest
import com.guulpay.enterOtp.model.OtpafterrigistrationRequest
import com.guulpay.enterOtp.model.VerifyOTPRequest
import com.guulpay.forgotPasscode.model.CreatePasswordRequest
import com.guulpay.forgotPasscode.model.ForgotPasswordRequest
import com.guulpay.forgotPasscode.model.ResendOTPRequest
import com.guulpay.guulex.model.BaseCurrencyRequest
import com.guulpay.guulexfinal.models.CurrencyListRequest
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListRequest
import com.guulpay.guulexfinal.models.CalculateAmountRequest
import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.kycverification.model.KYCRequestVerificationModel
import com.guulpay.login.models.LoginRequestModel
import com.guulpay.mobilerecharge.model.MobileRechargeRequest
import com.guulpay.mobilerecharge.model.MsisdninfoRequest
import com.guulpay.mobilerecharge.model.TransactionRequest
import com.guulpay.myProfile.model.*
import com.guulpay.notification.modelRechargelist.NotificationRequest
import com.guulpay.operatorList.models.OperatorListRequest
import com.guulpay.signup.model.SignUpRequest
import com.guulpay.transaction.model.TransactionHistoryRequest
import com.guulpay.transaction.model.TransactionHistoryResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/* All Retrofit interfaces here */
interface APIService {

    @POST("login")
    fun callLoginAPI(@Body loginRequestModel: LoginRequestModel): Observable<BaseResponse>

    @POST("register")
    fun callSignUpAPI(@Body signUpRequest: SignUpRequest): Observable<BaseResponse>

    @POST("otpafterrigistration")
    fun callotpafterrigistration(@Body otpafterrigistrationRequest: OtpafterrigistrationRequest): Observable<BaseResponse>

    @POST("loginafterrigistration")
    fun callloginafterrigistration(@Body enterOTPRequest: EnterOTPRequest): Observable<BaseResponse>

    @POST("forgotpassword")
    fun callForgotpasswordAPI(@Body forgotPasswordRequest: ForgotPasswordRequest): Observable<BaseResponse>

    @GET("getcallprefixcode")
    fun callCountryCodeAPI(): Observable<BaseResponse>

    @POST("getoperatorlist")
    fun callOperatorListAPI(@Body operatorListRequest: OperatorListRequest): Observable<BaseResponse>

    @POST("otpvalidation")
    fun callVerifyOTPAPI(@Body verifyOTPRequest: VerifyOTPRequest): Observable<BaseResponse>

    @POST("resendotp")
    fun callResendOTPAPI(@Body verifyOTPRequest: ResendOTPRequest): Observable<BaseResponse>

    @POST("createnewpassword")
    fun callcreatenewpasswordAPI(@Body createPasswordRequest: CreatePasswordRequest): Observable<BaseResponse>

    @POST("add-money-to-wallet")
    fun calladdmoenttowalletAPI(@Body addMoneyWalletRequest: AddMoneyWalletRequest): Observable<BaseResponse>

    @POST("getpages")
    fun callAboutusAPI(@Field("json_string") json_string: String): Observable<BaseResponse>

    @POST("mobilerecharge")
    fun callmobileRechargeApi(@Body mobileRechargeRequest: MobileRechargeRequest): Observable<BaseResponse>

    @POST("userforcelogin")
    fun callforceloginApi(@Body forceLoginRequest: ForceLoginRequest): Observable<BaseResponse>

    @POST("otpafterlogin")
    fun callotpafterloginApi(@Body verifyOTPRequest: VerifyOTPRequest): Observable<BaseResponse>

    @POST("getpages")
    fun callAboutusPostAPI(@Body aboutUsRequest: AboutUsRequest): Observable<BaseResponse>

    @POST("contactus")
    fun callContactusPostAPI(@Body contactUsRequest: ContactUsRequest): Observable<BaseResponse>

    @POST("profiledetails")
    fun callProfiledetailsPostAPI(@Body userProfileRequest: UserProfileRequest): Observable<BaseResponse>

    @POST("editprofile")
    fun callEditProfilePostAPI(@Body userEditRequest: UserEditRequest): Observable<BaseResponse>

    @Multipart
    @POST("editprofileimage")
    fun callProfileImagePostAPI(@Part image: MultipartBody.Part, @Query("user_id") user_id: String): Observable<BaseResponse>

    @POST("msisdninfo")
    fun callMsisdninfoApi(@Body mobileRechargeRequest: MsisdninfoRequest): Observable<BaseResponse>

    @POST("mobilerechargelist")
    fun callRechargehistApi(@Body transactionRequest: TransactionRequest): Observable<BaseResponse>

    @POST("foreign-exchange-list")
    fun callTransactionListAPI(@Body baseCurrencyRequest: BaseCurrencyRequest): Observable<BaseResponse>

    @POST("changepassword")
    fun callChangepasswordAPI(@Body contactUsRequest: ChangePasscodeRequest): Observable<BaseResponse>

    @POST("useremailvalidation")
    fun callVerifyEmailPostAPI(@Body userProfileRequest: UserEmailVerificationRequest): Observable<BaseResponse>

    @POST("kyc-verification")
    fun callKYCURLGeneratorPostAPI(@Body kYCRequestVerificationModel: KYCRequestVerificationModel): Observable<BaseResponse>

    @POST("kyc-methods")
    fun callKYCURLMethodPostAPI(@Body kYCMethodsCheckRequest: KYCMethodsCheckRequest): Observable<BaseResponse>

    @POST("useremailvalidation")
    fun callUserEmailValidate(@Body userEmailVerify: UserEmailVerify): Observable<BaseResponse>

    @POST("/guulpayapi/api/user-notification-list")
    fun callnotificationRequest(@Body notificationRequest: NotificationRequest): Observable<BaseResponse>



    /*
    added by Rishabh 23/1/2019

     */

    @POST("/guulpayapi/api/currencylist")
    fun callCurrencyList(@Body currencyList : CurrencyListRequest): Observable<BaseResponse>

    @POST("/guulpayapi/api/foreign-exchange-list")
    fun callForeignExchangeList(@Body forExList: ForExListRequest): Observable<BaseResponse>

    @POST("/guulpayapi/api/currencypairprice")
    fun callCalculateAmountList(@Body calculateAmount:CalculateAmountRequest):Observable<BaseResponse>

    @POST("/guulpayapi/api/addforexcartbuy")
    fun callForexCartBuy(@Body forexCartBuy: ForexCartBuyRequest):Observable<BaseResponse>

    @POST("/guulpayapi/api/transactiondetails")
    fun callTransactionHistory(@Body transactionHistory:TransactionHistoryRequest):Observable<BaseResponse>

}
