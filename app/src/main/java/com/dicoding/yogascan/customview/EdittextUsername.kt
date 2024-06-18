package com.dicoding.yogascan

import android.util.AttributeSet
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

class EdittextUsername : AppCompatEditText {

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val username = s.toString()
                if (isUsernameValid(username)) {
                    error = null
                } else {
                    error = context.getString(R.string.invalid_username_format)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isUsernameValid(username: String): Boolean {
        // Username should contain only letters, digits, and underscore (_), and should not include spaces
        val regex = "^[a-zA-Z0-9_]+$"
        return username.matches(regex.toRegex())
    }
}