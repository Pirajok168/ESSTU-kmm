package ru.esstu.android.authorized.settings.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.MainActivity
import ru.esstu.android.domain.ui.MainActivity.Companion.SELECTED_LOCALE_TAG
import ru.esstu.android.domain.ui.activity
import ru.esstu.android.shared.clearWindowInsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleSettingsScreen(
    paddingValues: PaddingValues,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val shared = context.activity.getSharedPreferences(
        MainActivity.SELECTED_LOCALE,
        AppCompatActivity.MODE_PRIVATE
    )
    val selectedLocale = shared.getString(SELECTED_LOCALE_TAG, "ru")
    Scaffold(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding()),
        contentWindowInsets = WindowInsets.clearWindowInsets(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.locale_app))
                },
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                },

            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ){
            Spacer(modifier = Modifier.size(16.dp))
            Surface(
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    Modifier
                        .selectableGroup()
                        .padding(vertical = 16.dp)
                ) {
                    ChooseItem(
                        title = stringResource(id = R.string.locale_app_ru),
                        selected = selectedLocale == "ru",
                        onClick = {

                            with(shared.edit()){
                                putString(SELECTED_LOCALE_TAG, "ru")
                                apply()
                            }
                            context.activity.recreate()
                        }
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    ChooseItem(
                        title = stringResource(id = R.string.locale_app_zh),
                        selected = selectedLocale == "zh",
                        onClick = {
                            with(shared.edit()){
                                putString(SELECTED_LOCALE_TAG, "zh")
                                apply()
                            }
                            context.activity.recreate()
                        }
                    )
                }


            }
        }
    }
}

@Composable
private fun ChooseItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
){
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = onClick
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title
                )
            },
            trailingContent = {
                RadioButton(
                    selected = selected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
                )
            }
        )
    }
}