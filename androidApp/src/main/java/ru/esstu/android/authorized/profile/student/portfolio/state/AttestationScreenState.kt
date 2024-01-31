package ru.esstu.android.authorized.profile.student.portfolio.state

import ru.esstu.features.profile.porfolio.domain.model.Attestation

data class AttestationScreenState(
    val loading: Boolean = true,
    val attestationList: List<Attestation> = emptyList(),
    val savedAttestationList: List<Attestation> = emptyList(),
    val error: Boolean = false
)
