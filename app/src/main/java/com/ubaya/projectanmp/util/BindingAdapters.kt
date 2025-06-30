package com.ubaya.projectanmp.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.Locale

@BindingAdapter("currency")
fun TextView.bindCurrency(amount: Long?) {
    if (amount == null) return
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    text = format.format(amount)
}