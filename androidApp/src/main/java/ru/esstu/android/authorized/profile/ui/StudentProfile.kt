package ru.esstu.android.authorized.profile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.ui.component.MainInfoProfile
import ru.esstu.android.authorized.profile.ui.component.PreviewPortfolioCard
import ru.esstu.android.domain.copyToClipboard
import ru.esstu.features.profile.main_profile.domain.model.Profile
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioType

@Composable
fun StudentProfile(
    state: Profile.StudentProfile,
    onNavigateAttestation: () -> Unit,
    onNavigatePortfolio: (StudentPortfolioType) -> Unit,
) {
    val context = LocalContext.current
    Spacer(modifier = Modifier.size(32.dp))
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AdditionalInfo(
            title = stringResource(id = R.string.group),
            content = state.group
        )

        AdditionalInfo(
            title = stringResource(id = R.string.course),
            content = state.course
        )

        AdditionalInfo(
            title = stringResource(id = R.string.form_education),
            content = state.studyForm
        )

        AdditionalInfo(
            title = stringResource(id = R.string.standart_education),
            content = state.standard
        )
        AdditionalInfo(
            title = stringResource(id = R.string.level_education),
            content = state.studyLevel
        )
    }
    Spacer(modifier = Modifier.size(32.dp))
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            MainInfoProfile(
                title = stringResource(id = R.string.movement),
                content = "${state.directionCode} ${state.directionName}",
                onClick = {
                    context.copyToClipboard("${state.directionCode} ${state.directionName}")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MainInfoProfile(
                title = stringResource(id = R.string.profile_education),
                content = state.profile,
                onClick = {
                    context.copyToClipboard(state.profile)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MainInfoProfile(
                title = stringResource(id = R.string.kafedra),
                content = state.cathedra,
                onClick = {
                    context.copyToClipboard(state.cathedra)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MainInfoProfile(
                title = stringResource(id = R.string.facultet),
                content = state.faculty,
                onClick = {
                    context.copyToClipboard(state.faculty)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }
    }
    Spacer(modifier = Modifier.size(32.dp))
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
    ) {

        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.attestation),
                onClick = {
                    onNavigateAttestation()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }


    }
    Spacer(modifier = Modifier.size(32.dp))
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.award),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.AWARD)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.ACHIEVEMENT),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.ACHIEVEMENT)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.TRAINEESHIP),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.TRAINEESHIP)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.REVIEWS),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.REVIEWS)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.WORK),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.WORK)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.SCIENCEREPORT),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.SCIENCEREPORT)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.THEME),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.THEME)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.SCIENCEWORK),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.SCIENCEWORK)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
    Spacer(modifier = Modifier.size(16.dp))

    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.CONFERENCE),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.CONFERENCE)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.CONTEST),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.CONTEST)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.EXHIBITION),
                onClick = {
                    onNavigatePortfolio(StudentPortfolioType.EXHIBITION)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}