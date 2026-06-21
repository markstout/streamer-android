package com.markstout.streamer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.markstout.streamer.data.local.entities.TitleEntity
import com.markstout.streamer.ui.theme.MovieTeal
import com.markstout.streamer.ui.theme.TvPurple

@Composable
fun TitleCard(
    title: TitleEntity,
    onClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = title.posterUrl,
                contentDescription = title.title,
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val badgeColor = if (title.type == "TV") TvPurple else MovieTeal
                    Badge(containerColor = badgeColor) {
                        Text(title.type, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    title.imdbId?.let { imdbId ->
                        Text(
                            text = "IMDb",
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    uriHandler.openUri("https://www.imdb.com/title/$imdbId/")
                                }
                                .padding(4.dp)
                        )
                    }
                }

                Text(
                    text = "Rating: ${title.rating ?: "N/A"}",
                    fontSize = 14.sp
                )
                
                Text(
                    text = title.releaseDate?.take(4) ?: "",
                    fontSize = 14.sp
                )
            }
        }
    }
}
