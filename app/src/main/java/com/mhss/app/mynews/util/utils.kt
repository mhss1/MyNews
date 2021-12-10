package com.mhss.app.mynews.util

fun String.countryToCode(): String {
    return when (this) {
        "Canada", "Egypt", "France" -> this.substring(0, 2).lowercase()
        "Germany" -> "de"
        "China" -> "cn"
        "United Arab Emirates" -> "ae"
        "United Kingdom" -> "gb"
        else -> "us"
    }
}

fun String.languageToCode(): String {
    return when (this) {
        "English", "Arabic", "French", "Italian" -> this.substring(0, 2).lowercase()
        "German" -> "de"
        "Spanish" -> "es"
        "Portuguese" -> "pt"
        "Chinese" -> "zh"
        else -> "en"
    }
}
