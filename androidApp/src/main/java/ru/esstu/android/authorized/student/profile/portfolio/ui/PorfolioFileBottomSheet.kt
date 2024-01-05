package ru.esstu.android.authorized.student.profile.portfolio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soywiz.klock.DateTime
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.format
import com.soywiz.klock.locale.russian
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioFileBottomSheet(
    bottomSheetState: SheetState,
    selectedType: PortfolioFile?,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onClose() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                                        text = it.date.format("dd MMMM yyyy", KlockLocale.russian),
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
                                        text = it.date.format("dd MMMM yyyy", KlockLocale.russian),
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
            }
        }
    }
}

@Composable
private fun DateElem(
    startDate: DateTime,
    endDate: DateTime
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = startDate.format("dd MMMM yyyy", KlockLocale.russian),
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
                    text = endDate.format("dd MMMM yyyy", KlockLocale.russian),
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