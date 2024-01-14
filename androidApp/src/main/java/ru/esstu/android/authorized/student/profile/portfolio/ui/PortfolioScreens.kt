package ru.esstu.android.authorized.student.profile.portfolio.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.esstu.android.R
import ru.esstu.android.authorized.student.profile.portfolio.viewmodel.PortfolioViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.domain.utill.workingDate.format
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    paddingValues: PaddingValues,
    portfolioViewModel: PortfolioViewModel,
    onBackPressed: () -> Unit
) {
    val state = portfolioViewModel.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var openBottomSheet by remember { mutableStateOf(false) }
    val openAddPortfolioBottomSheet = state.openAddPortfolioBottomSheet

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var selectedType: PortfolioFile? by remember {
        mutableStateOf(null)
    }

    var errorDialog by remember {
        mutableStateOf(false)
    }

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
                        portfolioViewModel.openAddedBottomSheet()
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
                                text = (file as? PortfolioFile.Conference)?.theme?.lowercase()
                                    ?: file.title,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 2
                            )
                        },
                        supportingContent = {
                            Text(
                                text = file.getSupportingContent(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        overlineContent = {
                            Text(
                                text = file.getOverlineContent()
                            )
                        },
                        modifier = Modifier.clickable {
                            selectedType = file
                            openBottomSheet = !openBottomSheet
                        }
                    )
                }
            }
        }

        if (state.listFiles.isEmpty() && !state.isLoad) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.absent_data),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }

        }
    }

    if (openBottomSheet) {
        PortfolioFileBottomSheet(
            bottomSheetState,
            selectedType,
            onClose = {
                openBottomSheet = false
            }
        )
    }

    if (openAddPortfolioBottomSheet) {
        AddedPortfolioBottomSheet(
            state,
            portfolioViewModel,
            onError = {
                errorDialog = true
            }
        )
    }

    if (errorDialog) {
        AlertDialog(
            onDismissRequest = {
                errorDialog = false
            },
            title = {
                Text(text = "Произошла ошибка")
            },
            text = {
                Text(text = "При загрузке файла произошла ошибка.\nПожалуйста, повторите попытку.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorDialog = false
                    }
                ) {
                    Text("Ок")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        errorDialog = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}


private fun PortfolioFile.getOverlineContent() = when (this) {
    is PortfolioFile.Achievement -> this.status
    is PortfolioFile.Award -> this.status
    is PortfolioFile.Conference -> "Coавторы: ${this.coauthors}"
    is PortfolioFile.Contest -> "${this.place} · ${this.result}"
    is PortfolioFile.Exhibition -> "${this.place} · ${this.exhibit}"
    is PortfolioFile.Reviews -> ""
    is PortfolioFile.ScienceReport -> ""
    is PortfolioFile.ScienceWorks -> this.type
    is PortfolioFile.Theme -> ""
    is PortfolioFile.Traineeship -> this.place
    is PortfolioFile.Work -> ""
}

private fun PortfolioFile.getSupportingContent() = when (this) {
    is PortfolioFile.Achievement -> this.date.format("dd MMMM yyyy")
    is PortfolioFile.Award -> this.date.format("dd MMMM yyyy")
    is PortfolioFile.Conference ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is PortfolioFile.Contest ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is PortfolioFile.Exhibition ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is PortfolioFile.Reviews -> this.type
    is PortfolioFile.ScienceReport -> this.status.orEmpty()
    is PortfolioFile.ScienceWorks -> "Coавторы: ${this.coauthors}"
    is PortfolioFile.Theme -> ""
    is PortfolioFile.Traineeship ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is PortfolioFile.Work -> this.type
}

@Composable
private fun PortfolioType.toTitle() = when (this) {
    PortfolioType.ACHIEVEMENT -> stringResource(id = R.string.ACHIEVEMENT)
    PortfolioType.CONFERENCE -> stringResource(id = R.string.CONFERENCE)
    PortfolioType.AWARD -> stringResource(id = R.string.award)
    PortfolioType.CONTEST -> stringResource(id = R.string.CONTEST)
    PortfolioType.EXHIBITION -> stringResource(id = R.string.EXHIBITION)
    PortfolioType.SCIENCEREPORT -> stringResource(id = R.string.SCIENCEREPORT)
    PortfolioType.WORK -> stringResource(id = R.string.WORK)
    PortfolioType.TRAINEESHIP -> stringResource(id = R.string.TRAINEESHIP)
    PortfolioType.REVIEWS -> stringResource(id = R.string.REVIEWS)
    PortfolioType.THEME -> stringResource(id = R.string.THEME)
    PortfolioType.SCIENCEWORK -> stringResource(id = R.string.SCIENCEWORK)
}