package com.guulpay.payments.scanQrCode

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.google.zxing.Result
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import com.guulpay.payments.PaymentActivity
import kotlinx.android.synthetic.main.scan_qr_fragment.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.alert
import android.content.Intent
import com.google.zxing.Reader
import com.guulpay.others.Constants
import com.google.zxing.MultiFormatReader
import android.provider.MediaStore
import android.graphics.Bitmap
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.BinaryBitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.LuminanceSource
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.widget.Toast
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.DecodeHintType
import com.google.zxing.BarcodeFormat
import java.util.*
import android.graphics.BitmapFactory
import android.R.attr.data
import android.util.Log
import com.guulpay.payments.pay.EnterAmountPayMoneyFragment


class ScanQrFragment : BaseFragment(), View.OnClickListener, ZXingScannerView.ResultHandler, ScanQrContract.View {
    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var scannedQrResult:String = ""
    var fetchedQrResultFromGallery = "Invalid QR code"
    lateinit var scanQrPresenter: ScanQrPresenter

    companion object {
        const val CLASS_NAME = "ScanQrFragment"
        fun newInstance(fromWhichFragment:String): Fragment {
            val fragment = ScanQrFragment()
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME,fromWhichFragment)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        zXingScannerView.stopCameraPreview()
        super.onDestroyView()
    }

    override fun getLayoutView(): Int {
        return R.layout.scan_qr_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        ScanQrPresenter(this).start()
        zXingScannerView.startCamera()
        zXingScannerView.setAutoFocus(true)
    }

    override fun initListeners() {
        imgvwOpenGallery.setOnClickListener(this)
        imgvwFlashOnOff.setOnClickListener(this)
        zXingScannerView.setResultHandler(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwFlashOnOff) {
            scanQrPresenter.handleCameraFlash()
        } else if (v == imgvwOpenGallery) {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select QR image"), Constants.RequestCodes.REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_GALLERY && data != null) {

            val pickedImageUri = data.data
            val imageStream = activity?.contentResolver?.openInputStream(pickedImageUri)
            val bitmap =  BitmapFactory.decodeStream(imageStream)

            val compressBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
            /* val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri) */

            val qrContent = readQrImage(compressBitmap)

            activity?.alert(qrContent, "Scanned Result") {
                isCancelable = false
                negativeButton("Dismiss"){
                    it.dismiss()
                    zXingScannerView.startCamera()
                    zXingScannerView.setAutoFocus(true)
                }
                positiveButton("Proceed"){
                    scanQrPresenter.afterScannedQr()
                }
            }?.show()
        }
    }

    private fun readQrImage(bitmap: Bitmap): String {
        /* Convert bitmap to binaryBitmap for decoding */
        val intArray = IntArray(bitmap.getWidth() * bitmap.getHeight())
        // copy pixel data from the Bitmap into the 'intArray' array
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight())

        val source = RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        try{
            fetchedQrResultFromGallery = MultiFormatReader().decode(binaryBitmap).text
        }
        catch (exception: Exception){
            Log.e("readQrImage",exception.toString())
        }

        return fetchedQrResultFromGallery
    }

    /* ZXingScannerView.ResultHandler */
    override fun handleResult(result: Result?) {
        scannedQrResult = result.toString()
        activity?.alert(scannedQrResult, "Scanned Result") {
            isCancelable = false
            negativeButton("Done") {
                it.dismiss()
                scanQrPresenter.afterScannedQr()
            }
        }?.show()
    }


    /* ScanQrContract.View callback methods */
    override fun flashOn() {
        imgvwFlashOnOff.background = activity?.resources?.getDrawable(R.drawable.ic_phone_flash_on_icon)
    }

    override fun flashOff() {
        imgvwFlashOnOff.background = activity?.resources?.getDrawable(R.drawable.ic_phone_flash_icon)
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as PaymentActivity).isActivityVisible
    }

    override fun setPresenter(presenter: ScanQrContract.Presenter) {
        scanQrPresenter = presenter as ScanQrPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNetworkUnavailableMsg() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContext(): Context {
        return context
    }
    override fun getArgument(): Bundle? {
        return arguments
    }
    override fun goToEnterAmtPayFragment() {
        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EnterAmountPayMoneyFragment.newInstance(),
                R.id.flPaymentContainer, false, EnterAmountPayMoneyFragment.CLASS_NAME)
    }
    override fun goToSendFragment() {
        activity?.onBackPressed()
    }
}