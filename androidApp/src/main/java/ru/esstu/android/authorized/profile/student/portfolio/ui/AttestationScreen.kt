package ru.esstu.android.authorized.profile.student.portfolio.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ru.esstu.android.R
import ru.esstu.android.authorized.profile.student.portfolio.viewmodel.AttestationViewModel
import ru.esstu.android.shared.clearWindowInsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttestationScreen(
    paddingValues: PaddingValues,
    attestationViewModel: AttestationViewModel = viewModel(),
    onBackPressed: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state = attestationViewModel.state
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.clearWindowInsets(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.attestation))
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Год обучения: 1") },
                            onClick = {
                                attestationViewModel.onChangeYear(1)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })

                        DropdownMenuItem(
                            text = { Text("Год обучения: 2") },
                            onClick = {
                                attestationViewModel.onChangeYear(2)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })

                        DropdownMenuItem(
                            text = { Text("Год обучения: 3") },
                            onClick = {
                                attestationViewModel.onChangeYear(3)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })

                        DropdownMenuItem(
                            text = { Text("Год обучения: 4") },
                            onClick = {
                                attestationViewModel.onChangeYear(4)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })

                        DropdownMenuItem(
                            text = { Text("Год обучения: 5") },
                            onClick = {
                                attestationViewModel.onChangeYear(5)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })

                        Divider()

                        DropdownMenuItem(
                            text = { Text("Все") },
                            onClick = {
                                attestationViewModel.onResetSort()
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Check,
                                    contentDescription = null
                                )
                            })
                    }
                },
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) {
        if (state.loading){
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment =  Alignment.Center){
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
            }
        }else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.attestationList.forEach {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = it.subject?.name ?: "—",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.padding(4.dp))

                            Text(
                                text = it.controlType?.name ?: "—",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(modifier = Modifier.padding(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Год обучения:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = it.eduYear?.let { it.toString() } ?: "—",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                )
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Блок:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = it.eduBlock?.let { it.toString() } ?: "—",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                )
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                tonalElevation = 8.dp,
                                color = MaterialTheme.colorScheme.secondary
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "${it.nameMarks ?:  "—"} / ${it.summaryGrade ?: "—"}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
        if (state.attestationList.isEmpty() && !state.loading) {
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
}
