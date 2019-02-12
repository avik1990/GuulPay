package com.guulpay.customUiComponents

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.guulpay.R

class CustomEditText : AppCompatEditText {

    fun setCustomFont(context: Context, customFont: String) {
        try {
            var typeface = Typeface.createFromAsset(context.assets, customFont)
            setTypeface(typeface)
        }
        catch (exception: Exception){
            Log.e("CustomEditText", exception.toString())
        }
    }

    constructor(context: Context):super(context){
        setCustomFont(context,context.getString(R.string.quicksandRegular))
    }

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        setCustomFont(context,context.getString(R.string.quicksandRegular))
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle:Int):super(context, attributeSet, defStyle){
        setCustomFont(context,context.getString(R.string.quicksandRegular))
    }
}