package ru.esstu.android.authorized.profile.student.portfolio.ui

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
import ru.esstu.android.authorized.profile.student.portfolio.ui.bottomsheet.AddedPortfolioBottomSheet
import ru.esstu.android.authorized.profile.student.portfolio.ui.bottomsheet.PortfolioFileBottomSheet
import ru.esstu.android.authorized.profile.student.portfolio.viewmodel.StudentPortfolioViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.domain.utill.workingDate.format
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPortfolioScreen(
    paddingValues: PaddingValues,
    portfolioViewModel: StudentPortfolioViewModel,
    onBackPressed: () -> Unit
) {
    val state = portfolioViewModel.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var openBottomSheet by remember { mutableStateOf(false) }
    val openAddPortfolioBottomSheet = state.openAddPortfolioBottomSheet

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var selectedType: StudentPortfolioFile? by remember {
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
                                text = (file as? StudentPortfolioFile.Conference)?.theme?.lowercase()
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


private fun StudentPortfolioFile.getOverlineContent() = when (this) {
    is StudentPortfolioFile.Achievement -> this.status
    is StudentPortfolioFile.Award -> this.status
    is StudentPortfolioFile.Conference -> "Coавторы: ${this.coauthors}"
    is StudentPortfolioFile.Contest -> "${this.place} · ${this.result}"
    is StudentPortfolioFile.Exhibition -> "${this.place} · ${this.exhibit}"
    is StudentPortfolioFile.Reviews -> ""
    is StudentPortfolioFile.ScienceReport -> ""
    is StudentPortfolioFile.ScienceWorks -> this.type
    is StudentPortfolioFile.Theme -> ""
    is StudentPortfolioFile.Traineeship -> this.place
    is StudentPortfolioFile.Work -> ""
}

private fun StudentPortfolioFile.getSupportingContent() = when (this) {
    is StudentPortfolioFile.Achievement -> this.date.format("dd MMMM yyyy")
    is StudentPortfolioFile.Award -> this.date.format("dd MMMM yyyy")
    is StudentPortfolioFile.Conference ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is StudentPortfolioFile.Contest ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is StudentPortfolioFile.Exhibition ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is StudentPortfolioFile.Reviews -> this.type
    is StudentPortfolioFile.ScienceReport -> this.status.orEmpty()
    is StudentPortfolioFile.ScienceWorks -> "Coавторы: ${this.coauthors}"
    is StudentPortfolioFile.Theme -> ""
    is StudentPortfolioFile.Traineeship ->
        "${
            this.startDate.format(
                "dd MMMM yyyy"
            )
        } — ${this.endDate.format("dd MMMM yyyy")}"

    is StudentPortfolioFile.Work -> this.type
}

@Composable
private fun StudentPortfolioType.toTitle() = when (this) {
    StudentPortfolioType.ACHIEVEMENT -> stringResource(id = R.string.ACHIEVEMENT)
    StudentPortfolioType.CONFERENCE -> stringResource(id = R.string.CONFERENCE)
    StudentPortfolioType.AWARD -> stringResource(id = R.string.award)
    StudentPortfolioType.CONTEST -> stringResource(id = R.string.CONTEST)
    StudentPortfolioType.EXHIBITION -> stringResource(id = R.string.EXHIBITION)
    StudentPortfolioType.SCIENCEREPORT -> stringResource(id = R.string.SCIENCEREPORT)
    StudentPortfolioType.WORK -> stringResource(id = R.string.WORK)
    StudentPortfolioType.TRAINEESHIP -> stringResource(id = R.string.TRAINEESHIP)
    StudentPortfolioType.REVIEWS -> stringResource(id = R.string.REVIEWS)
    StudentPortfolioType.THEME -> stringResource(id = R.string.THEME)
    StudentPortfolioType.SCIENCEWORK -> stringResource(id = R.string.SCIENCEWORK)
}