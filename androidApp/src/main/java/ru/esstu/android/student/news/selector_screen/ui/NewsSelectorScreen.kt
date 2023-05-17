package ru.esstu.android.student.news.selector_screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.esstu.android.student.news.selector_screen.ui.component.*
import ru.esstu.android.student.news.selector_screen.viewModel.SelectorScreenEvents
import ru.esstu.android.student.news.selector_screen.viewModel.SelectorViewModel
import kotlin.time.Duration.Companion.minutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soywiz.klock.DateFormat
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsEvents
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsViewModel

private val formatter: DateFormat = DateFormat("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSelectorScreen(
    onNavToAnnouncements: () -> Unit = {},
    onNavToNews: () -> Unit = {},
    onNavToEvents: () -> Unit = {},
    onNavToDetailScreen: () -> Unit = {},
    onNavToScheduleScreen: () -> Unit = {},

    announcementsViewModel: AnnouncementsViewModel = viewModel(),
   // newsViewModel: NewsViewModel = viewModel(),
   // eventsViewModel: EventsViewModel = viewModel(),
    viewModel: SelectorViewModel = viewModel(),
    parentPadding: PaddingValues,
) {
    Scaffold(
        modifier = Modifier
            .padding(parentPadding)
            .statusBarsPadding(),
        topBar = {
            MediumTopAppBar(
                title = {Text(text = "Главная ВСГУТУ")}
            )
//        TopAppBar(
//            elevation = 0.dp,
//            backgroundColor = MaterialTheme.colorScheme.background,
//            title = {  },
//        )
    }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            val uiState = viewModel.state
            val scope = rememberCoroutineScope()

            LaunchedEffect(key1 = Unit, block = {
                scope.launch {
                    while (true) {
                        //viewModel.onEvent(SelectorScreenEvents.PassTodayTime(LocalDateTime.now()))
                        delay(1.minutes)
                    }
                }
            })

            /*LaunchedEffect(key1 = uiState, block = {
                if (!uiState.isAnchorLoading && uiState.anchor == null)
                    viewModel.onEvent(SelectorScreenEvents.LoadAnchor)

                if (uiState.anchor != null && uiState.today != null && !uiState.isScheduleLoading && !uiState.isScheduleLoaded)
                    viewModel.onEvent(SelectorScreenEvents.LoadSchedule)
            })*/

            Spacer(modifier = Modifier.height(24.dp))

           /* if (uiState.todayWeek == Week.Uneven || uiState.todayWeek == Week.Even) {

                val weekTitle = when (uiState.todayWeek) {
                    Week.Uneven -> "Первая неделя"
                    Week.Even -> "Вторая неделя"
                    else -> ""
                }

                val weekNumber = when (uiState.todayWeek) {
                    Week.Uneven -> 1
                    Week.Even -> 2
                    else -> 0
                }

                DayNumberSelector(Modifier.padding(horizontal = 16.dp), weekNumber = weekNumber, weekTitle = weekTitle)

            }*/

            /*if (uiState.closestLesson != null && uiState.today != null) {
                val (period, nodes) = uiState.closestLesson

                Spacer(modifier = Modifier.height(20.dp))

                val isLessonRunning = period.inIt(uiState.today)
                val beforeLessonStart = Minutes.minutesBetween(uiState.today.toLocalTime(), period.startTime).toPeriod().normalizedStandard()
                val afterLessonStart = Minutes.minutesBetween(period.startTime, uiState.today.toLocalTime()).toPeriod().normalizedStandard()
                val beforeLessonEnd = Minutes.minutesBetween(uiState.today.toLocalTime(), period.endTime).toPeriod().normalizedStandard()

                if (isLessonRunning || !isLessonRunning && beforeLessonStart.hours == 0)
                    if (nodes.size == 1) {
                        val node = nodes.first()
                        ScheduleCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            lessonName = node.Subject,
                            lessonType = node.lessonType,
                            lessonTime = "${
                                timeFormatter.print(period.startTime)
                            } - ${
                                timeFormatter.print(period.endTime)
                            }${
                                if (isLessonRunning) {
                                    if (beforeLessonEnd.hours == 0)
                                        " | ${wordBasedFormatter.print(beforeLessonEnd)} до конца."
                                    else if (afterLessonStart.minutes == 0)
                                        " | пара началась."
                                    else
                                        " | идет ${wordBasedFormatter.print(afterLessonStart)}."
                                } else
                                    " | До начала ${wordBasedFormatter.print(beforeLessonStart)}."
                            }",
                            lessonNum = (node.lessonOrder.ordinal + 1).toString(),
                            auditoriumNum = node.AuditoryName,
                            professorName = node.ProfessorName,
                            onClick = {
                                onNavToScheduleScreen()
                            }
                        )
                    } else
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(nodes) { node ->
                                ScheduleCard(
                                    modifier = Modifier
                                        .width(260.dp),
                                    lessonName = node.Subject,
                                    lessonType = node.lessonType,
                                    lessonTime = "${
                                        timeFormatter.print(period.startTime)
                                    } - ${
                                        timeFormatter.print(period.endTime)
                                    }${
                                        if (isLessonRunning) {
                                            if (beforeLessonEnd.hours == 0)
                                                " | ${wordBasedFormatter.print(beforeLessonEnd)} до конца занятия."
                                            else if (afterLessonStart.minutes == 0)
                                                " | пара началась."
                                            else
                                                " | идет ${wordBasedFormatter.print(afterLessonStart)}."
                                        } else
                                            " | До начала ${wordBasedFormatter.print(beforeLessonStart)}."
                                    }",
                                    lessonNum = (node.lessonOrder.ordinal + 1).toString(),
                                    auditoriumNum = node.AuditoryName,
                                    professorName = node.ProfessorName,
                                    onClick = {
                                        onNavToScheduleScreen()
                                    }
                                )
                            }
                        }

            }*/
            Spacer(modifier = Modifier.height(28.dp))


            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Image(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_news_pattern_1),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Недавние объявления", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))


            val announcementsState = announcementsViewModel.state
            LaunchedEffect(key1 = Unit, block = {
                if (!announcementsState.isPagesLoading && announcementsState.pages.isEmpty())
                    announcementsViewModel.onEvent(AnnouncementsEvents.LoadAndRefresh)
            })
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    ShortNextCard(Modifier.clickable { onNavToAnnouncements() })
                }

                items(announcementsState.pages) { note ->
                    val preview = note.attachments.firstOrNull { img -> img.isImage }

                    Box() {
                        if (preview == null)
                            PreviewCard(
                                user = note.from,
                                title = note.title,
                                body = note.message,
                                onClick = {
                                    viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Объявление"))
                                    onNavToDetailScreen()
                                }
                            )
                        else
                            ImagePreviewCard(
                                photoUri = preview.closestUri,
                                user = note.from,
                                title = note.title,
                                onClick = {
                                    viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Объявление"))
                                    onNavToDetailScreen()
                                }
                            )
                    }
                }
                item {
                    NextCard(onClick = onNavToAnnouncements)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Image(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_news_pattern_2),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Новости университета", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))

            /*val newsState = newsViewModel.state
            LaunchedEffect(key1 = Unit, block = {
                if (!newsState.isPagesLoading && newsState.pages.isEmpty())
                    newsViewModel.onEvent(NewsEvents.LoadAndRefresh)
            })

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    ShortNextCard(Modifier.clickable { onNavToNews() })
                }
                items(newsState.pages) { note ->
                    val preview = note.attachments.firstOrNull { img -> img.isImage }

                    Box() {
                        if (preview == null)
                            PreviewCard(
                                user = note.from,
                                title = note.title,
                                body = note.message,
                                onClick = {
                                   // viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Новость"))
                                    onNavToDetailScreen()
                                }
                            )
                        else
                            ImagePreviewCard(
                                photoUri = preview.closestUri,
                                user = note.from,
                                title = note.title,
                                onClick = {
                                    //viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Новость"))
                                    onNavToDetailScreen()
                                }
                            )
                    }
                }
                item {
                    NextCard(onClick = onNavToNews)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Image(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_news_pattern_3),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Мероприятия", style = MaterialTheme.typography.h6)
            }
            Spacer(modifier = Modifier.height(16.dp))

            val eventsState = eventsViewModel.state
            LaunchedEffect(key1 = Unit, block = {
                if (!eventsState.isLoading && eventsState.recentEvents.isEmpty())
                    eventsViewModel.onEvent(EventsEvents.Load)
            })

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    ShortNextCard(Modifier.clickable { onNavToEvents() })
                }
                items(eventsState.recentEvents) { note ->
                    val preview = note.attachments.firstOrNull { img -> img.isImage }
                    Box() {
                        if (preview == null)
                            PreviewCard(
                                user = note.from,
                                title = note.title,
                                body = note.message,
                                onClick = {
                                   // viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Мероприятие"))
                                    onNavToDetailScreen()
                                }
                            )
                        else
                            ImagePreviewCard(
                                photoUri = preview.closestUri,
                                user = note.from,
                                title = note.title,
                                onClick = {
                                   // viewModel.onEvent(SelectorScreenEvents.PassNode(note, "Мероприятие"))
                                    onNavToDetailScreen()
                                }
                            )
                    }
                }
                item {
                    NextCard(onClick = onNavToEvents)
                }
            }*/
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}