package com.junkfood.seal.ui.page.downloadv2


import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader.DownloadTask.State.Status
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.CustomCommandTaskItem
import com.junkfood.seal.ui.component.FilterChipWithIcon
import com.junkfood.seal.ui.component.NavigationBarSpacer
import com.junkfood.seal.ui.component.TaskStatus
import com.junkfood.seal.ui.theme.SealTheme
import kotlinx.coroutines.delay

private const val TAG = "DownloadPageV2"

data class Chip(val index: Int = 0, val name: String = "")

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun DownloadPageImplV2(
    downloadCallback: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToDownloads: () -> Unit = {},
    showFilterChipGroup: Boolean = true,
    onSwitchView: (Boolean) -> Unit = {},
    isUsingGridView: Boolean = false,
    isScrollable: Boolean = true,
    content: @Composable () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val backgroundColor = MaterialTheme.colorScheme.surface
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {},
                modifier = Modifier,
                navigationIcon = {
                    TooltipBox(state = rememberTooltipState(),
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(text = stringResource(id = R.string.settings))
                            }
                        }) {
                        IconButton(
                            onClick = { navigateToSettings() }, modifier = Modifier
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = stringResource(id = R.string.settings)
                            )
                        }
                    }
                },
                actions = {
                    TooltipBox(state = rememberTooltipState(),
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            Text(text = stringResource(id = R.string.downloads_history))
                        }) {
                        IconButton(
                            onClick = { navigateToDownloads() }, modifier = Modifier
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Subscriptions,
                                contentDescription = stringResource(id = R.string.downloads_history)
                            )
                        }
                    }
                }, scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FABs(
                modifier = with(receiver = Modifier) { imePadding() },
                downloadCallback = downloadCallback,
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .then(
                    if (isScrollable) Modifier.verticalScroll(
                        rememberScrollState()
                    ) else Modifier
                )


        ) {
/*            Text(
                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp),
                text = "Download queue",
                style = MaterialTheme.typography.headlineSmall
            )*/
            Text(
                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp),
                text = "Seal",
                style = MaterialTheme.typography.displaySmall
            )


            Spacer(modifier = Modifier.height(12.dp))


            var chipList = remember {
                mutableStateListOf(
                    Chip(0, "Downloading"),
                    Chip(1, "Enqueued"),
                    Chip(2, "Canceled"),
                    Chip(3, "Completed"),
                    Chip(4, "Custom command")
                )
            }


            var selectedChip by remember { mutableStateOf(-1) }
            AnimatedVisibility(visible = showFilterChipGroup) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(chipList) { chip ->
                        val selected = chip.index == selectedChip
                        FilterChipWithIcon(
                            selected = selected,
                            onClick = {
                                selectedChip = if (selected) -1 else chip.index
                            },
                            label = chip.name,
                            selectedIcon = Icons.Outlined.Check
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "0 Download task", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(text = "Recently Added", style = MaterialTheme.typography.labelMedium)
//                    Icon(
//                        imageVector = Icons.Outlined.ArrowDropDown,
//                        contentDescription = null,
//                        modifier = Modifier.size(18.dp)
//                    )
//                }

                    IconButton(
                        onClick = { onSwitchView(!isUsingGridView) },
                        modifier = Modifier.size(32.dp)
                    ) {

                        Icon(
                            imageVector = if (isUsingGridView) Icons.AutoMirrored.Outlined.FormatListBulleted else Icons.Outlined.GridView,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (selectedChip == 4)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        CustomCommandTaskItem(status = TaskStatus.RUNNING)
                        CustomCommandTaskItem(status = TaskStatus.FINISHED)
                        CustomCommandTaskItem(status = TaskStatus.ERROR)
                        CustomCommandTaskItem(status = TaskStatus.CANCELED)
                    }


                Column(
                    Modifier
//                    .padding(horizontal = 24.dp)
                ) {
                    content()
//                    VideoCardPreview()
                }



                NavigationBarSpacer()
                Spacer(modifier = Modifier.height(160.dp))
            }

        }
    }
}


@Composable
fun FABs(
    modifier: Modifier = Modifier,
    downloadCallback: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(6.dp), horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = downloadCallback,
            content = {
                Icon(
                    Icons.Outlined.FileDownload,
                    contentDescription = stringResource(R.string.download)
                )
            },
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }

}


@Composable
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DownloadPageModePreview() {
    var b by remember {
        mutableStateOf(false)
    }
    SealTheme {
        Column() {
            DownloadPageImplV2(
                onSwitchView = { b = it },
                isUsingGridView = b
            ) {
                AnimatedContent(targetState = b) {
                    if (it)
                        VideoCardPreview()
                    else
                        VideoCardCompactPreview()
                }

            }
        }
    }
}


@Composable
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DownloadPageEmptyPreview() {
    var b by remember {
        mutableStateOf(false)
    }
    SealTheme {
        Column() {
            DownloadPageImplV2(
                onSwitchView = { b = it },
                showFilterChipGroup = false,
                isUsingGridView = b,
                isScrollable = false
            ) {
                BoxWithConstraints() {
                    Box(
                        modifier = Modifier
                            .height(maxHeight)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Manage your downloads here",
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
                /*               DownloadCard(modifier = Modifier
                                   .padding(horizontal = 20.dp)
                                   .padding(top = 24.dp))*/

            }
        }
    }
}

@Composable
//@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DownloadPageCompactPreview() {
    SealTheme {
        Column() {
            DownloadPageImplV2 {
                VideoCardCompactPreview()
            }
        }
    }
}


//@Preview
@Composable
private fun VideoCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 20.dp),

        ) {
        Spacer(modifier = Modifier.size(4.dp))
        val drawableList =
            listOf(R.drawable.sample, R.drawable.sample1, R.drawable.sample2, R.drawable.sample3)

        var progress by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(500)
                if (progress == 1f) progress = 0f else progress += 0.2f
            }
        }

        DownloadCard(status = Status.Enqueued, thumbnailUrl = drawableList[0])
        DownloadCard(status = Status.Running(progress, ""), thumbnailUrl = drawableList[1])
        DownloadCard(status = Status.Finished(emptyList()), thumbnailUrl = drawableList[2])
        DownloadCard(status = Status.Canceled, thumbnailUrl = drawableList[3])
        DownloadCard(status = Status.Error(""), thumbnailUrl = drawableList[3])
    }
}

@Composable
private fun VideoCardCompactPreview() {


    Column(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
    ) {
//        Spacer(modifier = Modifier.height(4.dp))
        val drawableList =
            listOf(
                R.drawable.sample,
                R.drawable.sample1,
                R.drawable.sample2,
                R.drawable.sample3
            )

        var progress by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(800)
                if (progress == 1f) progress = 0f else progress += 0.2f
            }
        }

        DownloadListItem(status = Status.Enqueued, thumbnailUrl = drawableList[0])
        DownloadListItem(
            status = Status.Running(progress, ""),
            thumbnailUrl = drawableList[1]
        )
        DownloadListItem(
            status = Status.Finished(emptyList()),
            thumbnailUrl = drawableList[2]
        )
        DownloadListItem(status = Status.Canceled, thumbnailUrl = drawableList[3])
        DownloadListItem(status = Status.Error(""), thumbnailUrl = drawableList[0])
        DownloadListItem(status = Status.FetchingInfo, thumbnailUrl = drawableList[1])
    }

}


