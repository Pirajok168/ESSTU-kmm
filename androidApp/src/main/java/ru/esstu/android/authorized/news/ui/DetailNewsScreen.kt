package ru.esstu.android.authorized.news.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.android.authorized.news.viewModel.SelectorViewModel
import ru.esstu.android.shared.shimmer.ShimmerBox
import ru.esstu.android.shared.shimmer.ShimmerLayout
import ru.esstu.android.R
import ru.esstu.android.authorized.news.events.SelectorScreenEvents


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailNewsScreen(
    parentPadding: PaddingValues,
    onBackPressed: () -> Unit,
    selectorViewModel: SelectorViewModel
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state = selectorViewModel.state
    val node = state.node ?: return
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val imageList by remember {
        mutableStateOf(node.attachments.filter { it.isImage })
    }
    val filesList by remember(state) {
        mutableStateOf(node.attachments)
    }
    val pagerState = rememberPagerState { imageList.count() }
    Scaffold(
        modifier = Modifier
            .padding(parentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                windowInsets = WindowInsets.clearWindowInsets(),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.clearWindowInsets()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (imageList.isNotEmpty()) {

                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 8.dp
                ) { page ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) {
                            Box(modifier = Modifier.matchParentSize()) {
                                GlideImage(
                                    imageModel = imageList[page].fileUri,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize(),
                                    loading = {
                                        ShimmerLayout(modifier = Modifier.matchParentSize()) {
                                            ShimmerBox(modifier = Modifier.matchParentSize())
                                        }
                                    }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(8.dp),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                ElevatedAssistChip(
                                    onClick = { },
                                    label = {
                                        Text(
                                            text = state.selected?.title!!
                                        )
                                    },
                                    elevation = AssistChipDefaults.elevatedAssistChipElevation(
                                        elevation = 16.dp
                                    )

                                )
                            }

                        }

                    }

                }

                Spacer(modifier = Modifier.size(16.dp))
            }


            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = node.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.size(16.dp))

                val annotatedString = buildAnnotatedString {

                    addStyle(
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background),
                        ).toSpanStyle(),
                        start = 0,
                        end = node.message.length
                    )
                    append(node.message)

                    val urlRegex = Regex("""(https?://[^\s]+)""")
                    val urls = urlRegex.findAll(node.message)

                    urls.forEach { result ->
                        val url = result.value
                        val start = result.range.first
                        val end = result.range.last + 1
                        addStyle(
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                                color = MaterialTheme.colorScheme.secondary
                            ).toSpanStyle(),
                            start = start,
                            end = end
                        )
                        addStringAnnotation(
                            tag = "URL",
                            annotation = url,
                            start = start,
                            end = end
                        )
                    }

                }
                SelectionContainer() {
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                val url = annotation.item
                                uriHandler.openUri(url)
                            }
                        },
                    )
                }

                filesList.forEach {
                    FileRow(it.name ?: "Изображение", it.loadProgress ?: 1f, it.localFileUri) {
                        uriHandler.openUri(it.fileUri)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Spacer(modifier = Modifier.size(24.dp))
            }

        }
    }
}

@Composable
private fun FileRow(
    title: String,
    loadProgress: Float,
    localUri: String?,
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
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            tonalElevation = 16.dp,
            modifier = Modifier.size(40.dp),
            shape = CircleShape
        ) {
            if (loadProgress < 1) {
                CircularProgressIndicator()
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter =
                        painterResource(id = if (localUri == null) R.drawable.ic_download else R.drawable.ic_chat_done),
                        contentDescription = null
                    )
                }
            }

        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}