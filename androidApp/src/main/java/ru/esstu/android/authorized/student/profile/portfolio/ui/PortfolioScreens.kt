package ru.esstu.android.authorized.student.profile.portfolio.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import com.soywiz.klock.DateTime
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.format
import com.soywiz.klock.locale.russian
import kotlinx.coroutines.launch
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatArguments
import ru.esstu.android.authorized.student.profile.navigation.ProfileScreens
import ru.esstu.android.authorized.student.profile.portfolio.viewmodel.PortfolioViewModel
import ru.esstu.android.domain.navigation.Route
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import java.util.Locale

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

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var selectedType: PortfolioFile? by remember {
        mutableStateOf(null)
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
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoad){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            } else {
                state.listFiles.forEach { file ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = (file as? PortfolioFile.Conference)?.theme?.lowercase() ?: file.title,
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
    }

    if (openBottomSheet){
        PortfolioFileBottomSheet(
            bottomSheetState,
            selectedType,
            onClose = {
                openBottomSheet = false
            }
        )
    }
}



private fun PortfolioFile.getOverlineContent() = when(this){
    is PortfolioFile.Achievement -> this.status
    is PortfolioFile.Award -> this.status
    is PortfolioFile.Conference -> "Coавторы: ${this.coauthors}"
    is PortfolioFile.Contest -> "${this.place} · ${this.result}"
    is PortfolioFile.Exhibition -> this.place
    is PortfolioFile.Reviews -> ""
    is PortfolioFile.ScienceReport -> ""
    is PortfolioFile.ScienceWorks -> this.type
    is PortfolioFile.Theme -> ""
    is PortfolioFile.Traineeship -> this.place
    is PortfolioFile.Work -> ""
}
private fun PortfolioFile.getSupportingContent() = when(this){
    is PortfolioFile.Achievement -> this.date.format("dd MMMM yyyy", KlockLocale.russian)
    is PortfolioFile.Award -> this.date.format("dd MMMM yyyy", KlockLocale.russian)
    is PortfolioFile.Conference ->
        "${this.startDate.format("dd MMMM yyyy", KlockLocale.russian)} — ${this.endDate.format("dd MMMM yyyy", KlockLocale.russian)}"
    is PortfolioFile.Contest ->
        "${this.startDate.format("dd MMMM yyyy", KlockLocale.russian)} — ${this.endDate.format("dd MMMM yyyy", KlockLocale.russian)}"
    is PortfolioFile.Exhibition ->
        "${this.startDate.format("dd MMMM yyyy", KlockLocale.russian)} — ${this.endDate.format("dd MMMM yyyy", KlockLocale.russian)}"

    is PortfolioFile.Reviews -> this.type
    is PortfolioFile.ScienceReport -> this.status.orEmpty()
    is PortfolioFile.ScienceWorks ->"Coавторы: ${this.coauthors}"
    is PortfolioFile.Theme -> ""
    is PortfolioFile.Traineeship ->
        "${this.startDate.format("dd MMMM yyyy", KlockLocale.russian)} — ${this.endDate.format("dd MMMM yyyy", KlockLocale.russian)}"
    is PortfolioFile.Work -> this.type
}
private fun PortfolioType.toTitle() = when(this){
    PortfolioType.ACHIEVEMENT -> "Достижения"
    PortfolioType.CONFERENCE -> "Участия в научных конференциях"
    PortfolioType.AWARD -> "Награды"
    PortfolioType.CONTEST -> "Участия в конкурсах, олимпиадах"
    PortfolioType.EXHIBITION ->"Участия в выставках"
    PortfolioType.SCIENCEREPORT -> "Научные доклады"
    PortfolioType.WORK -> "Курсовые, отчеты, рпз"
    PortfolioType.TRAINEESHIP -> "Стажировки"
    PortfolioType.REVIEWS -> "Рецензии"
    PortfolioType.THEME -> "Научные исследования"
    PortfolioType.SCIENCEWORK -> "Диссертации"
}