package com.dicoding.dicodingevents.utils

import org.jsoup.Jsoup

fun cleanerHtmlText(text : String) : String {
    return Jsoup.parse(text).text()
}