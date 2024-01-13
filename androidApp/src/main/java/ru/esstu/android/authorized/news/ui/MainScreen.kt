package ru.esstu.android.authorized.news.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.authorized.news.events.AnnouncementEvents
import ru.esstu.android.authorized.news.events.MainScreenEvents
import ru.esstu.android.authorized.news.events.SelectorScreenEvents
import ru.esstu.android.authorized.news.viewModel.AnnouncementViewModel
import ru.esstu.android.authorized.news.viewModel.MainScreenViewModel
import ru.esstu.android.authorized.news.viewModel.SelectorViewModel
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.android.shared.component.AuthorChip
import ru.esstu.android.shared.shimmer.ShimmerBox
import ru.esstu.android.shared.shimmer.ShimmerLayout
import ru.esstu.domain.utill.workingDate.format


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    parentPadding: PaddingValues,
    announcementViewModel: AnnouncementViewModel = viewModel(),
    mainScreenViewModel: MainScreenViewModel = viewModel(),
    selectorViewModel: SelectorViewModel = viewModel(),
    onDetailNews: () -> Unit
) {
    val announcementState = announcementViewModel.state
    val mainScreenState = mainScreenViewModel.state
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(key1 = Unit, block = {
        if (!announcementState.isPagesLoading && announcementState.pages.isEmpty())
            announcementViewModel.onEvent(AnnouncementEvents.LoadAndRefresh)

    })
    Scaffold(
        modifier = Modifier.padding(parentPadding).nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "" +
                        "Главная ВСГУТУ") },
                windowInsets = WindowInsets.clearWindowInsets(),
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets.clearWindowInsets()
    ) { padding ->
        if (!announcementState.isPagesLoading && announcementState.pages.isEmpty()) {
            ShimmerLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    (0..10).forEach {
                        ShimmerBox(
                            modifier = Modifier
                                .width(300.dp)
                                .height(40.dp)
                        )

                        ShimmerBox(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.dp)
                        )
                        ShimmerBox(
                            modifier = Modifier
                                .width(30.dp)
                                .height(5.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    LazyRow(
                        content = {
                            items(mainScreenState.filterNews) {
                                ElevatedFilterChip(
                                    selected = mainScreenState.selected == it,
                                    onClick = {
                                        mainScreenViewModel.onEvent(
                                            MainScreenEvents.ChooseOtherFilter(
                                                it
                                            )
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = it.title
                                        )
                                    }
                                )
                            }
                        },
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    )
                }
                itemsIndexed(announcementState.pages) { index, news ->
                    if (index == announcementState.pages.lastIndex && !announcementState.isEndReached && !announcementState.isPagesLoading)
                        announcementViewModel.onEvent(AnnouncementEvents.LoadNext)
                    if (news.attachments.any { it.isImage }) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = news.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 2
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = news.date.format("dd MMMM yyyy, HH:mm"),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            },
                            overlineContent = {
                                AuthorChip(author = "${news.from.shortFio} · ${news.from.summary}")
                            },
                            leadingContent = {
                                news.attachments.firstOrNull { it.isImage }?.let {
                                    GlideImage(
                                        imageModel = it.fileUri,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop,
                                        loading = {
                                            ShimmerLayout(modifier = Modifier.size(80.dp)) {
                                                ShimmerBox(modifier = Modifier.size(80.dp))
                                            }
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                            ) {
                                selectorViewModel.onEvent(SelectorScreenEvents.PassNode(news, mainScreenState.selected))
                                onDetailNews()
                            }
                        )
                    } else {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = news.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 2
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = news.date.format("dd MMMM yyyy, HH:mm"),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            },
                            overlineContent = {
                                AuthorChip(author = "${news.from.shortFio} · ${news.from.summary}")
                            },
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                            ) {
                                selectorViewModel.onEvent(SelectorScreenEvents.PassNode(news, mainScreenState.selected))
                                onDetailNews()
                            }
                        )
                    }
                }

                if (announcementState.isPagesLoading)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
            }
        }

    }
}


