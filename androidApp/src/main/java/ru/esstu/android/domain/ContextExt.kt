package ru.esstu.android.domain

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.getSystemService

fun Context.copyToClipboard(
    string: String,
    clipLabel: String = "$packageName.clipboard",
) {
    val clipboard = getSystemService<ClipboardManager>()
    val clip = ClipData.newPlainText(clipLabel, AnnotatedString(string))
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(this, "Скопировано", Toast.LENGTH_SHORT).show()
}