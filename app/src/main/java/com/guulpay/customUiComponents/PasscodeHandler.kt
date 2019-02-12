package com.guulpay.customUiComponents

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class PasscodeHandler(private val etPasscode1: CustomEditText, private val etPasscode2: CustomEditText,
                      private val etPasscode3: CustomEditText, private val etPasscode4: CustomEditText,
                      private val passcodeListener: PasscodeListener) : TextWatcher, View.OnKeyListener {

    init {
        etPasscode1.requestFocus()
        etPasscode2.isEnabled = false
        etPasscode3.isEnabled = false
        etPasscode4.isEnabled = false
        etPasscode1.addTextChangedListener(this)
        etPasscode2.addTextChangedListener(this)
        etPasscode2.setOnKeyListener(this)
        etPasscode3.addTextChangedListener(this)
        etPasscode3.setOnKeyListener(this)
        etPasscode4.addTextChangedListener(this)
        etPasscode4.setOnKeyListener(this)
    }

    interface PasscodeListener {
        fun onResult(status: Boolean)
    }

    /* Text Watcher listeners for Passcode */
    override fun afterTextChanged(s: Editable?) {
        if (s.toString().length == 1) {
            if (etPasscode1.isFocused && etPasscode1.text.toString().length > 0) {

                etPasscode2.requestFocus()

                //etPasscode1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                etPasscode2.isEnabled = true
                etPasscode3.isEnabled = false
                etPasscode4.isEnabled = false
            } else if (etPasscode2.isFocused && etPasscode2.text.toString().length > 0) {

                etPasscode3.requestFocus()
                etPasscode3.isEnabled = true
                etPasscode4.isEnabled = false
            } else if (etPasscode3.isFocused && etPasscode3.text.toString().length > 0) {

                etPasscode4.isEnabled = true
                etPasscode4.requestFocus()

            } else if (etPasscode4.isFocused) {
                if (etPasscode4.text.length > 0 && etPasscode3.text.length > 0 && etPasscode2.text.length > 0 && etPasscode1.text.length > 0)
                    passcodeListener.onResult(true)
            }
        }
    }

    override fun beforeTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    /* View.OnKeyListener callback method, to handle back button on keyboard */
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (etPasscode2.isFocused && etPasscode1.text.length > 0) {
                etPasscode1.requestFocus()
                etPasscode1.setText("")

                return true
            } else if (etPasscode3.isFocused && etPasscode2.text.length > 0) {
                etPasscode2.requestFocus()
                etPasscode2.setText("")

                return true
            } else if (etPasscode4.isFocused && etPasscode3.text.length > 0) {
                etPasscode3.requestFocus()
                etPasscode3.setText("")

                return true
            }

        }
        return false
    }

}