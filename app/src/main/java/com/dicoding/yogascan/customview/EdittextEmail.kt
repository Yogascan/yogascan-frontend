package com.dicoding.yogascan

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.yogascan.R

class EdittextEmail : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (isEmailValid(email)) {
                    error = null
                } else {
                    error = context.getString(R.string.invalid_email_format)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9_]+@gmail\\.com\$"
        return email.matches(emailPattern.toRegex())
    }
}