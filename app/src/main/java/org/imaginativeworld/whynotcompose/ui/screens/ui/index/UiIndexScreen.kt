package org.imaginativeworld.whynotcompose.ui.screens.ui.index

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.imaginativeworld.whynotcompose.ui.screens.AppComponent.Header
import org.imaginativeworld.whynotcompose.ui.screens.UIsScreen
import org.imaginativeworld.whynotcompose.ui.theme.AppTheme

@Composable
fun UiIndexScreen(
    navigate: (UIsScreen) -> Unit,
) {
    UiIndexSkeleton(
        navigate = navigate
    )
}

@Preview
@Composable
fun UiIndexSkeletonPreview() {
    AppTheme {
        UiIndexSkeleton()
    }
}

@Composable
fun UiIndexSkeleton(
    navigate: (UIsScreen) -> Unit = {},
) {

    Scaffold {

        Column(Modifier.fillMaxSize()) {
            Header("UIs")

            LazyColumn(Modifier.fillMaxSize()) {

                itemsIndexed(Ui.uiList) { index, item ->

                    if (index != 0) {
                        Divider(Modifier.padding(16.dp, 0.dp))
                    }

                    Text(
                        modifier = Modifier
                            .clickable {
                                navigate(item.route)
                            }
                            .padding(
                                start = 16.dp,
                                top = 8.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                            .fillMaxWidth(),
                        text = item.name
                    )
                }

            }
        }

    }
}