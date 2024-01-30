package ru.esstu.android.firebase.domain.model.admission_status

import ru.esstu.android.firebase.domain.model.admission_status.AdmissionStatus
import java.util.*

data class FirebaseAdmissionStatusResponse(
    val id: String,
    val title: String? = null,
    val time: Date,
    val status: AdmissionStatus
)