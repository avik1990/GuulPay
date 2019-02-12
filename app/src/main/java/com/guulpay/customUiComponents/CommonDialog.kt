package com.guulpay.customUiComponents

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.guulpay.R

class CommonDialog : DialogFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var dialogHeader: QuicksandMediumTextview
    private lateinit var dialogCancel: QuicksandMediumTextview
    private lateinit var dialogOk: QuicksandMediumTextview
    private lateinit var rdGroup: RadioGroup
    var checkpos: Int? = 0
    lateinit var rbbank: RadioButton
    lateinit var rbPasspost: RadioButton
    lateinit var rbDriving: RadioButton
    lateinit var rbNationalId: RadioButton

    companion object {
        private lateinit var mHeader: String
        private lateinit var mTitle: String
        private lateinit var mLeftBtn: String
        private lateinit var mRightBtn: String
        private var mIsCancelable: Boolean = true
        private lateinit var mListener: CommonDialogClickListener
        var isBank_: Boolean = false
        var isPassport_: Boolean = false
        var isDriving_: Boolean = false
        var isNational_: Boolean = false

        fun getInstance(header: String, title: String, leftCancel: String, rightOk: String, listener: CommonDialogClickListener): CommonDialog {
            val cardFragment = CommonDialog()
            mHeader = header
            mTitle = title
            mLeftBtn = leftCancel
            mRightBtn = rightOk
            mListener = listener
            return cardFragment
        }

        fun getInstance(header: String, title: String, leftCancel: String, rightOk: String, isCancelable: Boolean, listener: CommonDialogClickListener, isBank: Boolean, isPassport: Boolean, isDriving: Boolean, isNational: Boolean): CommonDialog {
            val cardFragment = CommonDialog()
            mHeader = header
            mTitle = title
            mLeftBtn = leftCancel
            mRightBtn = rightOk
            mListener = listener
            mIsCancelable = isCancelable
            isBank_ = isBank
            isPassport_ = isPassport
            isDriving_ = isDriving
            isNational_ = isNational
            return cardFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.rounded_corner_white_bg)
        val v = inflater.inflate(R.layout.dialogfragment_common, container, false)
        isCancelable = mIsCancelable
        initView(v)
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun initView(v: View) {
        dialogHeader = v.findViewById(R.id.dialog_header_TV)
        dialogCancel = v.findViewById(R.id.cancel_TV)
        dialogOk = v.findViewById(R.id.ok_TV)
        rdGroup = v.findViewById(R.id.rdGroup)
        dialogOk.isSelected = true

        rbbank = v.findViewById(R.id.rbbank)
        rbPasspost = v.findViewById(R.id.rbPasspost)
        rbDriving = v.findViewById(R.id.rbDriving)
        rbNationalId = v.findViewById(R.id.rbNationalId)


        if(isBank_){
            rbbank.visibility=View.VISIBLE
        }

        if(isPassport_){
            rbPasspost.visibility=View.VISIBLE
        }

        if(isDriving_){
            rbDriving.visibility=View.VISIBLE
        }

        if(isNational_){
            rbNationalId.visibility=View.VISIBLE
        }

        dialogHeader.text = mHeader
        dialogCancel.text = mLeftBtn
        dialogOk.text = mRightBtn

        dialogCancel.setOnClickListener(this)
        dialogOk.setOnClickListener(this)
        //onRadioButtonClicked(view)

        rdGroup?.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rbbank ->
                    checkpos = 0
                R.id.rbPasspost ->
                    checkpos = 1
                R.id.rbDriving ->
                    checkpos = 2
                R.id.rbNationalId ->
                    checkpos = 3
            }
        }

    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cancel_TV -> {
                if (!mIsCancelable)
                //  mListener.onLeftClick()
                    dismiss()
            }
            R.id.ok_TV -> {
                dismiss()
                mListener.onRightClick(checkpos!!)
            }
        }
    }

    interface CommonDialogClickListener {
        fun onRightClick()
        fun onRightClick(pos: Int)

    }

}