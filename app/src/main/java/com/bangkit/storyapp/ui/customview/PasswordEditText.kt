package com.bangkit.storyapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bangkit.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs), View.OnTouchListener {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if (it.length < 8){
                        setError(context.getString(R.string.alert_password))
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}