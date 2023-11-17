package com.junkfood.seal.ui.page.downloadv2

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.component.greenScheme
import com.junkfood.seal.util.toDurationText
import com.junkfood.seal.util.toFileSizeText

private const val PROGRESS_INDETERMINATE = -1f


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadCard(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.video_title_sample_text),
    author: String = stringResource(R.string.video_creator_sample_text),
    thumbnailUrl: Any = "",
    onClick: () -> Unit = {},
    status: Downloader.DownloadTask.State.Status = Downloader.DownloadTask.State.Status.FetchingInfo,
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
                    Downloader.DownloadTask.State.Status.Canceled -> {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.RestartAlt,
                                contentDescription = stringResource(
                                    id = R.string.restart
                                )
                            )
                        }
                    }

                    Downloader.DownloadTask.State.Status.Enqueued -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Downloader.DownloadTask.State.Status.Error -> {
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

                    Downloader.DownloadTask.State.Status.FetchingInfo -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Downloader.DownloadTask.State.Status.Finished -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = greenScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Downloader.DownloadTask.State.Status.Ready -> {
                        ProgressIconButton(progress = PROGRESS_INDETERMINATE) {}
                    }

                    is Downloader.DownloadTask.State.Status.Running -> {
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