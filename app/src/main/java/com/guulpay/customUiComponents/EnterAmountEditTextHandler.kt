package com.guulpay.customUiComponents

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher

class EnterAmountEditTextHandler(private val editText: CustomEditText, private val listener: EnterAmountEditTextListener) : TextWatcher {

    init {
        editText.addTextChangedListener(this)
        //editText.setText("$")
        Selection.setSelection(editText.text, editText.text.length)
    }

    interface EnterAmountEditTextListener {
        fun isFieldBlank(fieldIsBlank: Boolean, text: String)
    }

    override fun afterTextChanged(s: Editable?) {
        /* if (!s.toString().startsWith("$")) {
             editText.setText("$")
             Selection.setSelection(editText.text, editText.text.length)
             Log.e("afterTextChanged_if", s.toString().length.toString())
         } else {
             Log.e("afterTextChanged_else", s.toString().length.toString())
             if (s.toString().length <= 1) {
                 listener.isFieldBlank(true, "")
             } else {
                 val text = s.toString().substring(1)
                 listener.isFieldBlank(false, text)
             }
         }*/
        //Timber.d("StringLength" + " " + s.toString().length)
        if (s.toString().isEmpty()) {
            listener.isFieldBlank(true, "")
        } else {
            val text = s.toString().substring(1)
            listener.isFieldBlank(false, text)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}