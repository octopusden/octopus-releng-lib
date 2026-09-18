package org.octopusden.octopus.releng.utils

import java.text.SimpleDateFormat
import java.util.Date

val SHORT_DATE_FORMAT = SimpleDateFormat("yy/dd/MM HH:mm:ss")

fun Date.toPrettyString(): String = SHORT_DATE_FORMAT.format(this)
