package org.octopusden.octopus.releng.utils

import java.text.SimpleDateFormat
import java.util.*

val SHORT_DATE_FORMAT = SimpleDateFormat("yy/dd/MM HH:mm:ss")
fun Date.toPrettyString() : String {
    return SHORT_DATE_FORMAT.format(this)
}