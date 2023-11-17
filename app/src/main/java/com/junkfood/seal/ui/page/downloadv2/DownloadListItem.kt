package com.junkfood.seal.ui.page.downloadv2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.component.HorizontalDivider
import com.junkfood.seal.util.toDurationText
import com.junkfood.seal.util.toFileSizeText

@Composable
fun DownloadListItem(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.video_title_sample_text),
    author: String = stringResource(R.string.video_creator_sample_text),
    thumbnailUrl: Any = "",
    onClick: () -> Unit = {},
    status: Downloader.DownloadTask.State.Status = Downloader.DownloadTask.State.Status.FetchingInfo,
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
                .padding(end = 4.dp, bottom = 4.dp)
                .size(32.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}