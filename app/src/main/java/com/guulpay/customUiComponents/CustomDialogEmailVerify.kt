package com.guulpay.customUiComponents

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.guulpay.R

class CustomDialogEmailVerify : DialogFragment(), View.OnClickListener{
    private lateinit var mContext: Context
    private lateinit var dialogHeader: QuicksandMediumTextview
    private lateinit var dialogCancel: QuicksandMediumTextview
    private lateinit var dialogOk: QuicksandMediumTextview
    companion object {
        private lateinit var mHeader: String
        private lateinit var mTitle: String
        private lateinit var mLeftBtn: String
        private lateinit var mRightBtn: String
        private var mIsCancelable: Boolean = true
        private lateinit var mListener:CommonDialogClickEmailListener

        fun getInstance(header: String, title: String, leftCancel: String, rightOk: String, listener: CommonDialogClickEmailListener): CustomDialogEmailVerify {
            val cardFragment1 = CustomDialogEmailVerify()
            mHeader = header
            mTitle = title
            mLeftBtn = leftCancel
            mRightBtn = rightOk
            mListener = listener
            return cardFragment1
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.rounded_corner_white_bg)
        val v = inflater.inflate(R.layout.dialogfragment_email_verify, container, false)
       // isCancelable = CommonDialog.mIsCancelable
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

        dialogOk.isSelected = true
        dialogHeader.text = mHeader
        dialogCancel.text = mLeftBtn
        dialogOk.text = mRightBtn

        dialogCancel.setOnClickListener(this)
        dialogOk.setOnClickListener(this)

    }
        interface CommonDialogClickEmailListener {
            fun onLeftClick()
            fun onRightClick()
            //fun onRightClick(pos: Int)

        }
    private fun deSelectAll() {
        dialogOk.isSelected = false
        dialogCancel.isSelected = false
    }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.cancel_TV -> {
                    if (!mIsCancelable)
                        mListener.onLeftClick()
                    dismiss()
                }
                R.id.ok_TV -> {
                    dismiss()
                    mListener.onRightClick()
                }
            }
    }

}