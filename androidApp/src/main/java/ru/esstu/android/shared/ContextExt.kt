package ru.esstu.android.shared

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openDialer(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    startActivity(intent)
}