package ru.android.esstu.feature.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.android.esstu.core.theme.EsstuAppTheme
import ru.android.esstu.feature.news.componentUi.NewsCard


@Composable
fun NewsScreen() {
    Scaffold(

    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ){
                items(10){
                    NewsCard(  label ="Вебинары компании «Антиплагиат» в марте 2023 г.",
                        underLabel = "Расписание вебинаров компании «Антиплагиат» в марте 2023 г.\n"
                                + "03.03.2023  11:00  (МСК)  \n" +
                                "Авторам \"Норма и патология в современной публикационной деятельности\"",
                        publicationDate = "26.03.2023",
                        creator = "Еремин Д.А."
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewNewsScreen() {
    EsstuAppTheme{
        NewsScreen()
    }
}