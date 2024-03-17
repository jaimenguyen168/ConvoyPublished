package edu.temple.convoy.main_convoy.audio.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.temple.convoy.R
import edu.temple.convoy.ui.components.CustomRoundButton

@Composable
fun PlayerButton(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit,
    onPause: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomRoundButton(
            modifier = Modifier.fillMaxWidth(),
            icon = if (isPlaying) {
                R.drawable.baseline_pause_24
            } else {
                R.drawable.baseline_play_arrow_24
            },
            onClick = {
                isPlaying = if (isPlaying) {
                    onPause()
                    false
                } else {
                    onPlay()
                    true
                }
            }
        )
    }
}

@Preview
@Composable
fun PlayerPreview() {
    PlayerButton(onPlay = { }) {}
}