package ru.esstu.android.authorized.profile.portfolio.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import ru.esstu.android.R
import ru.esstu.domain.utill.workingDate.format
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioFileBottomSheet(
    bottomSheetState: SheetState,
    selectedType: PortfolioFile?,
    onClose: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    ModalBottomSheet(
        onDismissRequest = { onClose() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            selectedType?.let {
                when(it){
                    is PortfolioFile.Award -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.date.format("dd MMMM yyyy"),
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Дата",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.status,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Статус награды",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Achievement -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.date.format("dd MMMM yyyy"),
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Дата",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.status,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Статус достижения",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Conference -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование конференции",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.theme,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Тема доклада",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.coauthors,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Coавторы",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.place,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Место проведения",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        DateElem(it.startDate, it.endDate)
                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.status,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Статус конференции",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                    }
                    is PortfolioFile.Contest -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование мероприятия",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.place,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Место проведения",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.result,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Результат",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        DateElem(it.startDate, it.endDate)

                        Spacer(modifier = Modifier.size(16.dp))
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.status,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Статус мероприятия",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                    }
                    is PortfolioFile.Exhibition -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование выставки",
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.place,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Место проведения",
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.exhibit,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Экспонат",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        DateElem(it.startDate, it.endDate)
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Reviews -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование работы",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.type,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Тип работы",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.ScienceReport -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.ScienceWorks -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.type,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Тип",
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.coauthors,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Соавторы",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Theme -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Тема научного исследования",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Traineeship -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование стажировки",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.place,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Место прохождения",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        DateElem(it.startDate, it.endDate)
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                    is PortfolioFile.Work -> {
                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Наименование",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))

                        Surface(
                            tonalElevation = 32.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it.type,
                                        style = MaterialTheme.typography.titleLarge,

                                        )
                                },
                                overlineContent = {
                                    Text(
                                        text = "Тип",
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }

                it.attachment?.let {
                    FileRow(
                        it.name ?: "Изображение",
                        onClick = {
                            uriHandler.openUri(it.fileUri)
                        }
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
private fun FileRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ) {
                onClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            tonalElevation = 16.dp,
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    painter =
                    painterResource(id =  R.drawable.ic_download),
                    contentDescription = null
                )
            }

        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun DateElem(
    startDate: LocalDateTime,
    endDate: LocalDateTime
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = startDate.format("dd MMMM yyyy"),
                    style = MaterialTheme.typography.titleLarge,

                    )
            },
            overlineContent = {
                Text(
                    text = "Дата начала",
                )
            }
        )
    }
    Spacer(modifier = Modifier.size(16.dp))
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = endDate.format("dd MMMM yyyy"),
                    style = MaterialTheme.typography.titleLarge,

                    )
            },
            overlineContent = {
                Text(
                    text = "Дата окончания",
                )
            }
        )
    }
}