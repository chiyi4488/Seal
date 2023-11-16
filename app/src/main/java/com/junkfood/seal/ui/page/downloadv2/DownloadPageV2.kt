package com.junkfood.seal.ui.page.downloadv2


import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader
import com.junkfood.seal.Downloader.DownloadTask.State.Status
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.component.CustomCommandTaskItem
import com.junkfood.seal.ui.component.FilterChipWithIcon
import com.junkfood.seal.ui.component.HorizontalDivider
import com.junkfood.seal.ui.component.NavigationBarSpacer
import com.junkfood.seal.ui.component.TaskStatus
import com.junkfood.seal.ui.component.greenScheme
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.util.PreferenceUtil
import com.junkfood.seal.util.WELCOME_DIALOG
import com.junkfood.seal.util.toDurationText
import com.junkfood.seal.util.toFileSizeText
import kotlinx.coroutines.delay

private const val TAG = "DownloadPageV2"

data class Chip(val index: Int = 0, val name: String = "")

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun DownloadPageImplV2(
    downloaderState: Downloader.State,
    taskState: Downloader.DownloadTaskStateV1,
    viewState: DownloadViewModel.ViewState,
    errorState: Downloader.ErrorState,
    showVideoCard: Boolean = false,
    showOutput: Boolean = false,
    showDownloadProgress: Boolean = false,
    processCount: Int = 0,
    downloadCallback: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToDownloads: () -> Unit = {},
    onNavigateToTaskList: () -> Unit = {},
    cancelCallback: () -> Unit = {},
    onVideoCardClicked: () -> Unit = {},
    onUrlChanged: (String) -> Unit = {},
    isPreview: Boolean = false,
    onSwitchView: (Boolean) -> Unit = {},
    isUsingGridView: Boolean = false,
    content: @Composable () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val backgroundColor = MaterialTheme.colorScheme.surface
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor),
                modifier = Modifier.padding(horizontal = 8.dp),
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
                modifier = with(receiver = Modifier) { if (showDownloadProgress) this else this.imePadding() },
                downloadCallback = downloadCallback,
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ) {
            TitleWithProgressIndicator(showProgressIndicator = downloaderState is Downloader.State.FetchingInfo,
                isDownloadingPlaylist = downloaderState is Downloader.State.DownloadingPlaylist,
                showCancelOperation = downloaderState is Downloader.State.DownloadingPlaylist || downloaderState is Downloader.State.DownloadingVideo,
                currentIndex = downloaderState.run { if (this is Downloader.State.DownloadingPlaylist) currentItem else 0 },
                downloadItemCount = downloaderState.run { if (this is Downloader.State.DownloadingPlaylist) itemCount else 0 },
                onClick = {
                    cancelCallback()
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onLongClick = {
                    PreferenceUtil.encodeInt(WELCOME_DIALOG, 1)
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                })

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

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
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
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "35 Downloading", style = MaterialTheme.typography.labelMedium)
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
                            imageVector = if (isUsingGridView) Icons.AutoMirrored.Outlined.List else Icons.Outlined.GridView,
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
                    with(taskState) {
                        content()
//                    VideoCardPreview()
                        AnimatedVisibility(
                            modifier = Modifier.fillMaxWidth(),
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                            visible = progressText.isNotEmpty() && showOutput
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 12.dp),
                                text = progressText,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }



                    NavigationBarSpacer()
                    Spacer(modifier = Modifier.height(160.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TitleWithProgressIndicator(
    showProgressIndicator: Boolean = true,
    showCancelOperation: Boolean = true,
    isDownloadingPlaylist: Boolean = true,
    currentIndex: Int = 1,
    downloadItemCount: Int = 4,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Column(modifier = with(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp)
    ) {
        if (showCancelOperation) this.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) { onClick() } else this
    }) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = 3.dp)
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )
            AnimatedVisibility(visible = showProgressIndicator) {
                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp), strokeWidth = 3.dp
                    )
                }
            }
        }
        /*        AnimatedVisibility (visible = showCancelOperation) {
                    Text(
                        if (isDownloadingPlaylist) stringResource(R.string.playlist_indicator_text).format(
                            currentIndex,
                            downloadItemCount
                        )
                        else stringResource(R.string.downloading_indicator_text),
                        modifier = Modifier.padding(start = 12.dp, top = 3.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }*/
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
                downloaderState = Downloader.State.DownloadingVideo,
                taskState = Downloader.DownloadTaskStateV1(
                    title = stringResource(R.string.video_title_sample_text),
                    uploader = stringResource(id = R.string.video_creator_sample_text),
                    progress = 0.75f,

                    ),
                viewState = DownloadViewModel.ViewState(),
                errorState = Downloader.ErrorState(errorReport = "This is an error report!"),
                processCount = 2,
                isPreview = true,
                showDownloadProgress = true,
                showVideoCard = true,
                isUsingGridView = b,
                onSwitchView = { b = it }
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
private fun DownloadPageCompactPreview() {
    SealTheme {
        Column() {
            DownloadPageImplV2(
                downloaderState = Downloader.State.DownloadingVideo,
                taskState = Downloader.DownloadTaskStateV1(
                    title = stringResource(R.string.video_title_sample_text),
                    uploader = stringResource(id = R.string.video_creator_sample_text),
                    progress = 0.75f,

                    ),
                viewState = DownloadViewModel.ViewState(),
                errorState = Downloader.ErrorState(errorReport = "This is an error report!"),
                processCount = 2,
                isPreview = true,
                showDownloadProgress = true,
                showVideoCard = true
            ) {
                VideoCardCompactPreview()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCardV2(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.video_title_sample_text),
    author: String = stringResource(R.string.video_creator_sample_text),
    thumbnailUrl: Any = "",
    onClick: () -> Unit = {},
    status: Status = Status.FetchingInfo,
    fileSizeApprox: Double = 1024 * 1024 * 69.0,
    duration: Int = 359,
    isPreview: Boolean = LocalInspectionMode.current
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column {
            Box(Modifier.fillMaxWidth()) {
                AsyncImageImpl(
                    modifier = Modifier
                        .padding()
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f, matchHeightConstraintsFirst = false),
                    model = thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    isPreview = isPreview
                )
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                    color = Color.Black.copy(alpha = 0.68f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    val fileSizeText = fileSizeApprox.toFileSizeText()
                    val durationText = duration.toDurationText()
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "$fileSizeText · $durationText",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .padding(vertical = 8.dp)
            ) {


                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = author,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                when (status) {
                    Status.Canceled -> {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.RestartAlt,
                                contentDescription = stringResource(
                                    id = R.string.restart
                                )
                            )
                        }
                    }

                    Status.Enqueued -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Status.Error -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = stringResource(
                                    id = R.string.copy_error_report
                                ),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Status.FetchingInfo -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Status.Finished -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = greenScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Status.Ready -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Status.Running -> {
                        ProgressIconButton(progress = status.progress) {}
                    }
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCardCompactV2(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.video_title_sample_text),
    author: String = stringResource(R.string.video_creator_sample_text),
    thumbnailUrl: Any = "",
    onClick: () -> Unit = {},
    status: Status = Status.FetchingInfo,
    fileSizeApprox: Double = 1024 * 1024 * 69.0,
    duration: Int = 359,
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 20.dp, end = 16.dp)
                .padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
//                        .padding(vertical = 12.dp)
//                        .padding(start = 12.dp)
            ) {
                AsyncImageImpl(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(80.dp)
                        .aspectRatio(16 / 9f, matchHeightConstraintsFirst = true)
                        .clip(MaterialTheme.shapes.extraSmall),
                    model = thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                    color = Color.Black.copy(alpha = 0.68f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    val fileSizeText = fileSizeApprox.toFileSizeText()
                    val durationText = duration.toDurationText()
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "$fileSizeText · $durationText",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }

            }

            Column(
                modifier = Modifier.padding(start = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f, false))
                StatusBar(
                    status = status,
                    modifier = Modifier.padding(top = 3.dp),
                    textStyle = MaterialTheme.typography.labelSmall,
                    iconSize = DpSize(14.dp, 14.dp)
                )
            }

        }

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }



    HorizontalDivider()


}


@Preview
@Composable
private fun VideoCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 24.dp),

        ) {
        Spacer(modifier = Modifier.size(8.dp))
        val drawableList =
            listOf(R.drawable.sample, R.drawable.sample1, R.drawable.sample2, R.drawable.sample3)

        var progress by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(500)
                if (progress == 1f) progress = 0f else progress += 0.2f
            }
        }

        VideoCardV2(status = Status.Enqueued, thumbnailUrl = drawableList[0])
        VideoCardV2(status = Status.Running(progress, ""), thumbnailUrl = drawableList[1])
        VideoCardV2(status = Status.Finished(emptyList()), thumbnailUrl = drawableList[2])
        VideoCardV2(status = Status.Canceled, thumbnailUrl = drawableList[3])
        VideoCardV2(status = Status.Error(""), thumbnailUrl = drawableList[3])
    }
}

@Preview
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

        VideoCardCompactV2(status = Status.Enqueued, thumbnailUrl = drawableList[0])
        VideoCardCompactV2(
            status = Status.Running(progress, ""),
            thumbnailUrl = drawableList[1]
        )
        VideoCardCompactV2(
            status = Status.Finished(emptyList()),
            thumbnailUrl = drawableList[2]
        )
        VideoCardCompactV2(status = Status.Canceled, thumbnailUrl = drawableList[3])
        VideoCardCompactV2(status = Status.Error(""), thumbnailUrl = drawableList[0])
        VideoCardCompactV2(status = Status.FetchingInfo, thumbnailUrl = drawableList[1])
    }

}

private const val PROGRESS_INDETERMINATE = -1f

@Composable
private fun ProgressIconButton(
    modifier: Modifier = Modifier,
    progress: Float = PROGRESS_INDETERMINATE,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        if (progress >= 0) {
            val progressAnimatedValue by animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                label = "Progress"
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                strokeWidth = 3.dp,
                progress = progressAnimatedValue,
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
        IconButton(
            onClick = onClick, modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Rounded.Stop,
                contentDescription = stringResource(id = R.string.cancel),
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}