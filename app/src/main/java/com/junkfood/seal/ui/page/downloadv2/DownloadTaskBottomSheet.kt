package com.junkfood.seal.ui.page.downloadv2

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Downloading
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.component.HorizontalDivider
import com.junkfood.seal.ui.component.SealModalBottomSheet
import com.junkfood.seal.ui.component.greenScheme
import com.junkfood.seal.ui.theme.SealTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadTaskBottomSheet(taskState: Downloader.DownloadTask.State, onClick: () -> Unit = {}) {
    SealModalBottomSheet(
        onDismissRequest = { /*TODO*/ }, sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = LocalDensity.current,
            initialValue = SheetValue.Expanded
        ), horizontalPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageWeight = 0.3f
            AsyncImageImpl(
                model = taskState.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .weight(imageWeight)
                    .aspectRatio(16 / 9f, matchHeightConstraintsFirst = true)
//                    .clip(MaterialTheme.shapes.extraSmall)
                ,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1 - imageWeight)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = taskState.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = taskState.uploader,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                StatusBar(status = taskState.status)

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        LazyColumn {
            item {
                BottomSheetAction(
                    icon = Icons.Outlined.AutoFixHigh,
                    text = "Change status"
                ) { onClick() }
            }
            item {
                BottomSheetAction(
                    icon = Icons.Outlined.ContentCopy,
                    text = stringResource(id = R.string.copy_link)
                ) {}
            }
            item {
                BottomSheetAction(icon = Icons.Outlined.Image, text = "View thumbnail") {}
            }
            item {
                BottomSheetAction(icon = Icons.Outlined.Delete, text = "Remove from queue") {}
            }
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    SealTheme {

        val statusList = listOf(
            Downloader.DownloadTask.State.Status.Enqueued,
            Downloader.DownloadTask.State.Status.FetchingInfo,
            Downloader.DownloadTask.State.Status.Ready,
            Downloader.DownloadTask.State.Status.Running(0.73f),
            Downloader.DownloadTask.State.Status.Finished(emptyList()),
            Downloader.DownloadTask.State.Status.Canceled,
            Downloader.DownloadTask.State.Status.Error(""),
        )
        var statusIndex by remember { mutableIntStateOf(0) }

        DownloadTaskBottomSheet(
            taskState = Downloader.DownloadTask.State(
                title = stringResource(
                    id = R.string.video_title_sample_text
                ),
                uploader = stringResource(id = R.string.video_creator_sample_text),
                status = statusList[statusIndex]
            )
        ) {
            if (statusIndex < statusList.size - 1) {
                statusIndex += 1
            } else {
                statusIndex = 0
            }
        }

    }
}

@Composable
fun BottomSheetAction(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = text, style = MaterialTheme.typography.titleSmall)
    }
}

@Preview
@Composable
private fun StatusBarPreview() {
    SealTheme {
        Surface {
            Column {
                StatusBar(status = Downloader.DownloadTask.State.Status.Enqueued)
                StatusBar(status = Downloader.DownloadTask.State.Status.FetchingInfo)
                StatusBar(status = Downloader.DownloadTask.State.Status.Ready)
                StatusBar(status = Downloader.DownloadTask.State.Status.Running(0.73f))
                StatusBar(status = Downloader.DownloadTask.State.Status.Finished(emptyList()))
                StatusBar(status = Downloader.DownloadTask.State.Status.Canceled)
                StatusBar(status = Downloader.DownloadTask.State.Status.Error(""))


            }
        }
    }
}

@Composable
fun StatusBar(status: Downloader.DownloadTask.State.Status) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 3.dp)
    ) {
        StatusBarIcon(status = status)
        StatusBarText(status = status)

    }


}


@Composable
private fun StatusBarIcon(status: Downloader.DownloadTask.State.Status) {
    when (status) {
        Downloader.DownloadTask.State.Status.Canceled -> {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                contentDescription = null
            )
        }

        Downloader.DownloadTask.State.Status.Enqueued -> {
            Icon(
                imageVector = Icons.Outlined.Downloading,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                contentDescription = null
            )
        }

        is Downloader.DownloadTask.State.Status.Error -> {
            Icon(
                imageVector = Icons.Outlined.Error,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }

        Downloader.DownloadTask.State.Status.FetchingInfo -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp), strokeWidth = 2.5f.dp
            )
        }

        is Downloader.DownloadTask.State.Status.Finished -> {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                contentDescription = null,
                tint = greenScheme.primary
            )
        }

        Downloader.DownloadTask.State.Status.Ready -> {
            Icon(
                imageVector = Icons.Outlined.Downloading,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                contentDescription = null
            )
        }

        is Downloader.DownloadTask.State.Status.Running -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(14.dp),
                progress = status.progress,
                strokeWidth = 2.5f.dp
            )
        }
    }

}

@Composable
fun StatusBarText(status: Downloader.DownloadTask.State.Status) {
    val textColor = when (status) {
        is Downloader.DownloadTask.State.Status.Error -> {
            MaterialTheme.colorScheme.error
        }

        is Downloader.DownloadTask.State.Status.Finished -> {
            greenScheme.primary
        }

        is Downloader.DownloadTask.State.Status.Running -> {
            MaterialTheme.colorScheme.primary
        }

        else -> {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    }
    val text = when (status) {
        Downloader.DownloadTask.State.Status.Canceled -> {
            stringResource(id = R.string.status_canceled)
        }

        Downloader.DownloadTask.State.Status.Enqueued -> {
            stringResource(id = R.string.status_enqueued)
        }

        is Downloader.DownloadTask.State.Status.Error -> {
            stringResource(id = R.string.status_error)
        }

        Downloader.DownloadTask.State.Status.FetchingInfo -> {
            stringResource(id = R.string.status_fetching_video_info)
        }

        is Downloader.DownloadTask.State.Status.Finished -> {
            stringResource(id = R.string.status_completed)
        }

        Downloader.DownloadTask.State.Status.Ready -> {
            stringResource(id = R.string.status_enqueued)
        }

        is Downloader.DownloadTask.State.Status.Running -> {
            "%.1f%%".format(status.progress * 100f)
        }
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )


}