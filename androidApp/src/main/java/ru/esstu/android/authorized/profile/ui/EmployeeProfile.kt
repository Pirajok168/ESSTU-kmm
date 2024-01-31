package ru.esstu.android.authorized.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.ui.component.MainInfoProfile
import ru.esstu.android.authorized.profile.ui.component.PreviewPortfolioCard
import ru.esstu.features.profile.main_profile.domain.model.Profile
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType


@Composable
fun EmployeeProfile(
    state: Profile.EmployeeProfile,
    onNavigatePortfolio: (EmployeePortfolioType) -> Unit,
) {
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
                title = stringResource(id = R.string.info_work),
                content = state.ranks.joinToString("\n"),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MainInfoProfile(
                title = stringResource(id = R.string.education),
                content = state.education.map {
                    it.institutionName.orEmpty() + ", " + stringResource(id = R.string.speciality) +
                            " "  +  "\"${it.speciality.orEmpty()}\"" + ", " + stringResource(id = R.string.qualification) + " " +
                            "\"${it.qualification.orEmpty()}\""
                }.joinToString("\n"),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MainInfoProfile(
                title = stringResource(id = R.string.degrees),
                content = state.degrees.map {
                    it.degreeName + ", " + stringResource(id = R.string.speciality) + " " +
                            "\"${it.code} — ${it.scientificSpeciality}\""
                }.joinToString("\n"),
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
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.education),
                onClick = {
                    onNavigatePortfolio(EmployeePortfolioType.Educations)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PreviewPortfolioCard(
                title = stringResource(id = R.string.add_education),
                onClick = {
                    onNavigatePortfolio(EmployeePortfolioType.AddEducation)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PreviewPortfolioCard(
                title = stringResource(id = R.string.award),
                onClick = {
                    onNavigatePortfolio(EmployeePortfolioType.Award)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }
    }
}