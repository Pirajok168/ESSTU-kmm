package ru.esstu.android.authorized.profile.portfolio.state

import ru.esstu.student.profile.student.porfolio.domain.model.Attestation

data class AttestationScreenState(
    val loading: Boolean = true,
    val attestationList: List<Attestation> = emptyList(),
    val savedAttestationList: List<Attestation> = emptyList(),
    val error: Boolean = false
)
