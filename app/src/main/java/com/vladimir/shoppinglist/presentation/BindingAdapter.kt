package com.vladimir.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.vladimir.shoppinglist.R

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}