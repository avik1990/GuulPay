package com.guulpay.customUiComponents

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.graphics.Typeface
import android.util.Log
import android.widget.TextView
import com.guulpay.R


class QuicksandMediumTextview : AppCompatTextView {

    fun setCustomFont(context: Context?, customFont: String?, isBold: Boolean) {
        try {
            var typeface = Typeface.createFromAsset(context?.assets, customFont)
            if (isBold)
                setTypeface(typeface, Typeface.BOLD)
            else
                setTypeface(typeface)
        } catch (exception: Exception) {
            Log.e("QuicksandMediumTextview", exception.toString())
        }
    }

    constructor(context: Context) : super(context) {
        setCustomFont(context, context.getString(R.string.quicksandMedium), false)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setCustomFont(context, context.getString(R.string.quicksandMedium), false)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        setCustomFont(context, context.getString(R.string.quicksandMedium), false)
    }
}