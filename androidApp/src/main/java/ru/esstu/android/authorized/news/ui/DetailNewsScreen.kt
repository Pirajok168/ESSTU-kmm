package ru.esstu.android.authorized.news.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import ru.esstu.android.shared.clearWindowInsets
import ru.esstu.android.authorized.news.viewModel.SelectorViewModel
import ru.esstu.android.shared.openDialer

@OptIn(ExperimentalMaterial3Api::class)
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
                    FilledIconButton(onClick = onBackPressed) {
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
                .padding(horizontal = 8.dp)
        ){
            Text(
                text = node.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.size(16.dp))

            val annotatedString = buildAnnotatedString {
                append(node.message)
                addStyle(
                    style = MaterialTheme.typography.bodyMedium.toSpanStyle(),
                    0, node.message.length
                )
                val urlRegex = Regex("""(https?://[^\s]+)""")
                val urls = urlRegex.findAll(node.message)

                urls.forEach { result ->
                    val url = result.value
                    val start = result.range.first
                    val end = result.range.last + 1
                    addStyle(
                        style = SpanStyle(textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.secondary),
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
                    }
                )
            }



        }
    }

}