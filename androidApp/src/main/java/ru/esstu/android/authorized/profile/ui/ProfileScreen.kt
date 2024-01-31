package ru.esstu.android.authorized.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.viewmodel.ProfileScreenViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.features.profile.main_profile.domain.model.Profile
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    viewModelProfile: ProfileScreenViewModel = viewModel(),
    onNavigateAttestation: () -> Unit,
    onNavigateStudentPortfolio: (StudentPortfolioType) -> Unit,
    onNavigateEmployeePortfolio: (EmployeePortfolioType) -> Unit
) {
    val state = viewModelProfile.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.clearWindowInsets(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.profile))
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (state.profileInfo == null) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                Surface(
                    tonalElevation = 4.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(100.dp),
                            shape = CircleShape,
                            tonalElevation = 32.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.profileInfo.initials(),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = state.profileInfo.fio(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                when (state.profileInfo) {
                    is Profile.EmployeeProfile -> {
                        EmployeeProfile(state.profileInfo, onNavigateEmployeePortfolio)
                    }
                    is Profile.StudentProfile -> {
                        StudentProfile(state.profileInfo,onNavigateAttestation, onNavigateStudentPortfolio)
                    }
                }
            }

        }

    }
}

@Composable
fun AdditionalInfo(
    title: String,
    content: String
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = content,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

    }
}




