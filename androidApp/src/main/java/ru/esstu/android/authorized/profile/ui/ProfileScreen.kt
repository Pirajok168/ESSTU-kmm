package ru.esstu.android.authorized.profile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.viewmodel.ProfileScreenViewModel
import ru.esstu.android.domain.copyToClipboard
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.student.profile.student.main_profile.domain.model.Profile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    viewModelProfile: ProfileScreenViewModel = viewModel(),
    onNavigateAttestation: () -> Unit,
    onNavigatePortfolio: (PortfolioType) -> Unit,
) {
    val state = viewModelProfile.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
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
                                    content = state.profileInfo.ranks.joinToString("\n"),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MainInfoProfile(
                                    title = stringResource(id = R.string.education),
                                    content = state.profileInfo.education.map {
                                        it.institutionName.orEmpty() + ", " + stringResource(id = R.string.speciality) +
                                             " "  +  "\"${it.speciality.orEmpty()}\"" + ", " + stringResource(id = R.string.qualification) + " " +
                                                "\"${it.qualification.orEmpty()}\""
                                    }.joinToString("\n"),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MainInfoProfile(
                                    title = stringResource(id = R.string.degrees),
                                    content = state.profileInfo.degrees.map {
                                        it.degreeName + ", " + stringResource(id = R.string.speciality) + " " +
                                                "\"${it.code} — ${it.scientificSpeciality}\""
                                    }.joinToString("\n"),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                            }
                        }
                    }
                    is Profile.StudentProfile -> {
                        Spacer(modifier = Modifier.size(32.dp))
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AdditionalInfo(
                                title = stringResource(id = R.string.group),
                                content = state.profileInfo.group
                            )

                            AdditionalInfo(
                                title = stringResource(id = R.string.course),
                                content = state.profileInfo.course
                            )

                            AdditionalInfo(
                                title = stringResource(id = R.string.form_education),
                                content = state.profileInfo.studyForm
                            )

                            AdditionalInfo(
                                title = stringResource(id = R.string.standart_education),
                                content = state.profileInfo.standard
                            )
                            AdditionalInfo(
                                title = stringResource(id = R.string.level_education),
                                content = state.profileInfo.studyLevel
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
                                    content = "${state.profileInfo.directionCode} ${state.profileInfo.directionName}",
                                    onClick = {
                                        context.copyToClipboard("${state.profileInfo.directionCode} ${state.profileInfo.directionName}")
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MainInfoProfile(
                                    title = stringResource(id = R.string.profile_education),
                                    content = state.profileInfo.profile,
                                    onClick = {
                                        context.copyToClipboard(state.profileInfo.profile)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MainInfoProfile(
                                    title = stringResource(id = R.string.kafedra),
                                    content = state.profileInfo.cathedra,
                                    onClick = {
                                        context.copyToClipboard(state.profileInfo.cathedra)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MainInfoProfile(
                                    title = stringResource(id = R.string.facultet),
                                    content = state.profileInfo.faculty,
                                    onClick = {
                                        context.copyToClipboard(state.profileInfo.faculty)
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
                                        onNavigatePortfolio(PortfolioType.AWARD)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.ACHIEVEMENT),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.ACHIEVEMENT)
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
                                        onNavigatePortfolio(PortfolioType.TRAINEESHIP)
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
                                        onNavigatePortfolio(PortfolioType.REVIEWS)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.WORK),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.WORK)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.SCIENCEREPORT),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.SCIENCEREPORT)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.THEME),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.THEME)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.SCIENCEWORK),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.SCIENCEWORK)
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
                                        onNavigatePortfolio(PortfolioType.CONFERENCE)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.CONTEST),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.CONTEST)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                PreviewPortfolioCard(
                                    title = stringResource(id = R.string.EXHIBITION),
                                    onClick = {
                                        onNavigatePortfolio(PortfolioType.EXHIBITION)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
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

@Composable
fun MainInfoProfile(
    title: String,
    content: String,
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        onClick?.let {
            ListItem(
                headlineContent = {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.titleLarge,

                        )
                },
                overlineContent = {
                    Text(
                        text = title,
                    )
                },
                trailingContent = {
                    FilledTonalButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = null
                        )
                    }
                }
            )
        } ?:  ListItem(
            headlineContent = {
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleLarge,

                    )
            },
            overlineContent = {
                Text(
                    text = title,
                )
            }
        )

    }
}

@Composable
fun PreviewPortfolioCard(
    title: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = onClick
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,

                    )
            },
            trailingContent = {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ) {
                    Icon(
                        contentDescription = null,
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        )
    }
}

