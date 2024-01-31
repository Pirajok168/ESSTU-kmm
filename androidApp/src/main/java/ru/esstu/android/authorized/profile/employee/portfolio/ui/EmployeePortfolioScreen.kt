package ru.esstu.android.authorized.profile.employee.portfolio.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.employee.portfolio.viewmodel.EmployeePortfolioViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeePortfolioScreen(
    paddingValues: PaddingValues,
    portfolioViewModel: EmployeePortfolioViewModel,
    onBackPressed: () -> Unit
) {
    val state = portfolioViewModel.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.clearWindowInsets(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.type?.toTitle().orEmpty())
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    FilledTonalIconButton(onClick = {
                        // portfolioViewModel.openAddedBottomSheet()
                    }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoad) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                state.listFiles.forEach { file ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = file.headlineContent(),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                        supportingContent = {
                            Text(
                                text = file.supportingContent(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        overlineContent = {
                            Text(
                                text = file.overlineContent(),
                            )
                        }
                    )
                }
            }
        }
    }
}


private fun EmployeePortfolioFile.supportingContent(): String = when(this){
    is EmployeePortfolioFile.Education -> "${this.educationType} ${Typography.bullet} ${this.qualification}"
    is EmployeePortfolioFile.AddEducation -> "${this.place} ${Typography.bullet} ${this.receiptDate}"
    is EmployeePortfolioFile.Award -> this.organization
}

private fun EmployeePortfolioFile.overlineContent(): String = when(this){
    is EmployeePortfolioFile.Education -> "${this.institution} ${Typography.bullet} ${this.receiptDate}"
    is EmployeePortfolioFile.AddEducation -> this.type
    is EmployeePortfolioFile.Award -> "${this.type} ${Typography.bullet} ${this.scaleAward} ${Typography.bullet} ${this.awardDate}"
}

private fun EmployeePortfolioFile.headlineContent(): String = when(this){
    is EmployeePortfolioFile.Education -> this.speciality
    is EmployeePortfolioFile.AddEducation -> this.name
    is EmployeePortfolioFile.Award -> this.awardName
}

@Composable
private fun EmployeePortfolioType.toTitle(): String = when(this){
    EmployeePortfolioType.Educations -> stringResource(id = R.string.education)
    EmployeePortfolioType.AddEducation -> stringResource(id = R.string.add_education)
    EmployeePortfolioType.Award -> stringResource(id = R.string.award)
}
