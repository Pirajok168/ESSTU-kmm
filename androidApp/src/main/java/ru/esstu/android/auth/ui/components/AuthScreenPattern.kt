package ru.esstu.android.auth.ui.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.domain.ui.theme.tertiary
import ru.esstu.domain.utill.wrappers.ResponseError


@Composable
fun AuthScreenPattern(
    title: String,
    subtitle: String,
    textFieldLabel: String,
    textFieldText: String,
    onTextFieldChange: (String) -> Unit,
    textFieldVisualTransformation: VisualTransformation = VisualTransformation.None,
    buttonText: String,
    isLoadingState: Boolean = false,
    error: ResponseError? = null,
    hideBackButton: Boolean = false,
    onBackPressed: () -> Unit = {},
    onNavToNext: () -> Unit = {}
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box {
            if (!hideBackButton)
                IconButton(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 12.dp),
                    onClick = onBackPressed
                ) {
                    Icon(
                        modifier = Modifier.padding(0.dp),
                        tint = MaterialTheme.colors.primary,
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        modifier = Modifier.paddingFromBaseline(128.dp),
                        text = title,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h4
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            modifier = Modifier.paddingFromBaseline(32.dp),
                            text = subtitle,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                    var isFocusRequested by rememberSaveable {
                        mutableStateOf(false)
                    }

                    val focusManager = LocalFocusManager.current
                    val focusRequester = remember { FocusRequester() }

                    LaunchedEffect(Unit) {
                        if (!isFocusRequested) {
                            isFocusRequested = true
                            focusRequester.requestFocus()
                        }
                    }

                    OutlinedTextField(
                        isError = error != null,
                        enabled = !isLoadingState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        visualTransformation = textFieldVisualTransformation,
                        label = {
                            Text(text = textFieldLabel)
                        }, colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colors.tertiary
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            onNavToNext()
                        }),
                        trailingIcon = {

                            val alpha by animateFloatAsState(targetValue = if (textFieldText.isNotEmpty()) 1f else 0f)

                            if (alpha > 0)
                                IconButton(
                                    enabled = !isLoadingState,
                                    modifier = Modifier.alpha(alpha),
                                    onClick = {
                                        onTextFieldChange("")
                                        focusRequester.requestFocus()
                                    }) {
                                    Icon(
                                        Icons.Default.Close,
                                        tint = if (error != null) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                                        contentDescription = null
                                    )
                                }
                        },
                        value = textFieldText,
                        onValueChange = { onTextFieldChange(it) }
                    )
                    Column(
                        modifier = Modifier
                            .height(28.dp)
                            .fillMaxWidth()
                    ) {
                        if (error != null) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .paddingFromBaseline(16.dp),
                                text = if (error.code == 400 || error.code == 401) "Неверно введен логин или пароль" else "Неизвестная ошибка, попробуйте позже",
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                            )
                        }
                    }

                    var buttonModifier: Modifier = Modifier
                    if (isLoadingState)
                        buttonModifier = buttonModifier.shimmer()

                    Button(
                        enabled = !isLoadingState,
                        modifier = buttonModifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        onClick = {
                            focusManager.clearFocus()
                            onNavToNext()
                        }
                    ) {
                        Text(text = buttonText)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        modifier = Modifier
                            .offset(x = 20.dp, y = 84.dp)
                            .scale(1.2f),
                        painter = painterResource(id = R.drawable.ic_auth_logo_borderless),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        alignment = Alignment.TopStart
                    )
                }
            }
        }
    }
}
