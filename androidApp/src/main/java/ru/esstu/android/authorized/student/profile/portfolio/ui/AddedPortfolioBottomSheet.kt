package ru.esstu.android.authorized.student.profile.portfolio.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.soywiz.klock.DateTime
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.format
import com.soywiz.klock.locale.russian
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.authorized.student.profile.portfolio.state.PortfolioState
import ru.esstu.android.authorized.student.profile.portfolio.viewmodel.PortfolioViewModel
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddedPortfolioBottomSheet(
    state: PortfolioState,
    portfolioViewModel: PortfolioViewModel,
    onError: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            portfolioViewModel.closeAddedBottomSheet()
        },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        if (state.isLoad) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                state.type?.let {
                    when (it) {
                        PortfolioType.AWARD,
                        PortfolioType.ACHIEVEMENT -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onSetDate,
                                placeholder = "Дата"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            StatusCard(
                                onSetStatus = portfolioViewModel::onSetStatus,
                                placeholder = if (it == PortfolioType.AWARD) "Статус награды" else "Статус достижения"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            FileCard(
                                files = state.attachments,
                                onChooseFile = {
                                    portfolioViewModel.onPassAttachments(it)
                                },
                                onRemoveFile = {
                                    portfolioViewModel.onRemoveAttachment(it)
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.CONFERENCE -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование конференции")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.theme,
                                    onValueChange = {
                                        portfolioViewModel.onSetTheme(it)
                                    },
                                    placeholder = {
                                        Text("Тема доклада")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.coathor,
                                    onValueChange = {
                                        portfolioViewModel.onInputCoathor(it)
                                    },
                                    placeholder = {
                                        Text("Coавторы")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            PlaceCard(state.place, portfolioViewModel::onInputPlace)

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputStartDate,
                                placeholder = "Дата начала"
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputEndDate,
                                placeholder = "Дата окончания"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            StatusCard(
                                onSetStatus = portfolioViewModel::onSetStatus,
                                placeholder = "Статус конференции"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.CONTEST -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование мероприятия")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            PlaceCard(state.place, portfolioViewModel::onInputPlace)
                            Spacer(modifier = Modifier.size(16.dp))
                            ResultCard(state.result, portfolioViewModel::onInputResult)

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputStartDate,
                                placeholder = "Дата начала"
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputEndDate,
                                placeholder = "Дата окончания"
                            )
                            Spacer(modifier = Modifier.size(16.dp))

                            StatusCard(
                                onSetStatus = portfolioViewModel::onSetStatus,
                                placeholder = "Статус мероприятия"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.EXHIBITION -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование выставки")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                            PlaceCard(state.place, portfolioViewModel::onInputPlace)
                            Spacer(modifier = Modifier.size(16.dp))


                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.exhibit,
                                    onValueChange = {
                                        portfolioViewModel.onInputExhibit(it)
                                    },
                                    placeholder = {
                                        Text("Экспонат")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputStartDate,
                                placeholder = "Дата начала"
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputEndDate,
                                placeholder = "Дата окончания"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.SCIENCEREPORT -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.WORK -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))

                            StatusCard(
                                "Тип",
                                listOf(
                                    "Курсовая работа",
                                    "Курсовой проект",
                                    "Отчет по практике",
                                    "Отчет по НИР",
                                    "Выпускная квалификационная работа"
                                ),
                                portfolioViewModel::onInputTypeWork
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.TRAINEESHIP -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование стажировки")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                            PlaceCard(state.place, portfolioViewModel::onInputPlace)
                            Spacer(modifier = Modifier.size(16.dp))

                            DateCard(
                                onSetDate = portfolioViewModel::onInputStartDate,
                                placeholder = "Дата начала"
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            DateCard(
                                onSetDate = portfolioViewModel::onInputEndDate,
                                placeholder = "Дата окончания"
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.REVIEWS -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование работы")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.typeWork,
                                    onValueChange = {
                                        portfolioViewModel.onInputTypeWork(it)
                                    },
                                    placeholder = {
                                        Text("Тип работы")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.THEME -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Тема научного исследования")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        PortfolioType.SCIENCEWORK -> {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.name,
                                    onValueChange = {
                                        portfolioViewModel.onInputName(it)
                                    },
                                    placeholder = {
                                        Text("Наименование")
                                    },

                                    )
                            }

                            Spacer(modifier = Modifier.size(16.dp))

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.typeWork,
                                    onValueChange = {
                                        portfolioViewModel.onInputTypeWork(it)
                                    },
                                    placeholder = {
                                        Text("Тип")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    value = state.coathor,
                                    onValueChange = {
                                        portfolioViewModel.onInputCoathor(it)
                                    },
                                    placeholder = {
                                        Text("Coавторы")
                                    },

                                    )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }

                DoneButton(
                    enable = portfolioViewModel.isEnabledDoneButton(),
                    onSend = {
                        portfolioViewModel.onSaveFiles(
                            onError = {
                                onError()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultCard(
    result: String,
    onInputResult: (String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = result,
            onValueChange = {
                onInputResult(it)
            },
            placeholder = {
                Text("Результат")
            },

            )
    }
}

@Composable
private fun PlaceCard(
    place: String,
    onInputPlace: (String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = place,
            onValueChange = {
                onInputPlace(it)
            },
            placeholder = {
                Text("Место проведения")
            },

            )
    }
}

@Composable
private fun DoneButton(
    enable: Boolean,
    onSend: () -> Unit
) {
    Button(
        onClick = onSend, enabled = enable,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Сохранить")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateCard(
    onSetDate: (Long) -> Unit,
    placeholder: String
) {
    var openDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = datePickerState.selectedDateMillis?.let {
                DateTime(it).format("dd MMMM yyyy", KlockLocale.russian)
            }.orEmpty(),
            onValueChange = {},
            placeholder = {
                Text(placeholder)
            },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    openDialog = !openDialog
                }) {
                    Icon(Icons.Rounded.DateRange, "")
                }
            }
        )
    }

    if (openDialog) {
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onSetDate)
                        openDialog = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileCard(
    files: List<CachedFile>,
    onChooseFile: (files: List<CachedFile>) -> Unit,
    onRemoveFile: (CachedFile) -> Unit
) {
    val context = LocalContext.current
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUris = result.data?.run {
                    when (val uris = clipData) {
                        null ->
                            listOfNotNull(data)

                        else ->
                            (0 until uris.itemCount).mapNotNull { index -> uris.getItemAt(index).uri }
                    }
                } ?: return@rememberLauncherForActivityResult

                onChooseFile(resultUris.mapNotNull { it.cacheToFile(context) })
            }
        }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        TextButton(
            onClick = {
                fileLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                })
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Edit, contentDescription = "")
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Загрузить файл")
            }
        }

        LazyRow {
            items(files) { attachment ->
                NewAttachment(
                    modifier = Modifier
                        .animateItemPlacement(
                            animationSpec = tween(durationMillis = 500)
                        )
                        .padding(vertical = 16.dp),
                    title = "${attachment.name}.${attachment.ext}",
                    uri = if (attachment.isImage) attachment.uri else null,
                    onClose = {
                        onRemoveFile(attachment)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusCard(
    placeholder: String,
    options: List<String> = listOf(
        "Федеральный",
        "Региональный", "Зарубежный", "Международный"
    ),
    onSetStatus: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                value = selectedOptionText.orEmpty(),
                onValueChange = {},
                placeholder = {
                    Text(placeholder)
                },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onSetStatus(selectionOption)
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

    }
}