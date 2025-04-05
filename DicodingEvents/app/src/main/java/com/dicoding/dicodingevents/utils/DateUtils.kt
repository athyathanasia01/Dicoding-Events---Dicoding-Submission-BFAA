package com.dicoding.dicodingevents.utils

import java.text.SimpleDateFormat
import java.util.*

fun changeFormatDate(date : String) : String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
    val dateParse = inputFormat.parse(date)
    return outputFormat.format(dateParse!!)
}