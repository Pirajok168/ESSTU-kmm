package ru.esstu.android.authorized.student.profile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R
import ru.esstu.android.authorized.student.profile.viewmodel.StudentProfileScreenViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    paddingValues: PaddingValues,
    viewModelProfile: StudentProfileScreenViewModel = viewModel(),
    onNavigatePortfolio: (PortfolioType) -> Unit
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
                    Text(text = "Профиль")
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (state.studentInfo == null){

        }else {
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
                                    text = state.studentInfo.initials,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = state.studentInfo.fio,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.size(32.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdditionalInfo(
                        title = "Группа",
                        content = state.studentInfo.group
                    )

                    AdditionalInfo(
                        title = "Курс",
                        content = state.studentInfo.course
                    )

                    AdditionalInfo(
                        title = "Форма обучения",
                        content = state.studentInfo.studyForm
                    )

                    AdditionalInfo(
                        title = "Образовательный стандарт",
                        content = state.studentInfo.standard
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
                            title = "Направление",
                            content = "${state.studentInfo.directionCode} ${state.studentInfo.directionName}",
                            onClick = {

                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        MainInfoProfile(
                            title = "Кафедра",
                            content = state.studentInfo.cathedra,
                            onClick = {

                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        MainInfoProfile(
                            title = "Факультет",
                            content = state.studentInfo.faculty,
                            onClick = {

                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        MainInfoProfile(
                            title = "Профиль",
                            content = state.studentInfo.profile,
                            onClick = {

                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        MainInfoProfile(
                            title = "Уровень образования",
                            content = state.studentInfo.studyLevel,
                            onClick = {

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
                            title = "Награды",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.AWARD)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Достижения",
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
                            title = "Стажировки",
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
                            title = "Рецензии",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.REVIEWS)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Курсовые, отчеты, рпз",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.WORK)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Научные доклады",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.SCIENCEREPORT)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Научные исследования",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.THEME)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Диссертации",
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
                            title = "Участие в научных конференциях, семинарах",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.CONFERENCE)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Участие в конкурсах, олимпиадах, гранты",
                            onClick = {
                                onNavigatePortfolio(PortfolioType.CONTEST)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        PreviewPortfolioCard(
                            title = "Участие в выставках",
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
    onClick: () -> Unit,
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
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

