package edu.temple.convoy.main_convoy.audio.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.temple.convoy.R
import edu.temple.convoy.ui.components.CustomRoundButton

@Composable
fun AudioMessageItem (
    audioMessage: AudioMessage,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    viewModel: AudioPlayerViewModel
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val selectedAudioMessage by viewModel.selectedAudioMessage.collectAsState()

    val nowPlaying = isPlaying && audioMessage == selectedAudioMessage

    val color = if (audioMessage.hasBeenPlayed) Color.LightGray else MaterialTheme.colors.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = color, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 16.dp),
            ) {
                CustomRoundButton(
                    modifier = Modifier.wrapContentSize(),
                    icon = if (nowPlaying) {
                        R.drawable.baseline_pause_24
                    } else {
                        R.drawable.baseline_play_arrow_24
                    },
                    onClick = {
                        if (nowPlaying) {
                            onStop()
                        } else {
                            onPlay()
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    text = audioMessage.username,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )

                if (nowPlaying) {
                    Text(
                        text = "Now playing...",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                }
            }
        }
    }
}
