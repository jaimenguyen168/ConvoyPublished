package edu.temple.convoy.main_convoy.audio.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.temple.convoy.R
import edu.temple.convoy.ui.components.CustomRoundButton
import java.io.File

@Composable
fun RecordingView(
    outputFile: File?,
    onRecord: () -> Unit,
    onStop: () -> Unit,
    onSave: () -> Unit,
    onSend: () -> Unit,
) {
    var isRecording by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        CustomRoundButton(
            modifier = Modifier.weight(1f),
            icon = R.drawable.baseline_save
        ) {
            onSave()
        }

        PlayerButton(
            modifier = Modifier.weight(1f),
            onPlay = { onRecord() },
            onPause = { onStop() }
        )

        CustomRoundButton(
            modifier = Modifier.weight(1f),
            icon = R.drawable.baseline_send_24
        ) {
            onSend()
        }
    }
}

@Preview
@Composable
fun RecordingDialogPreview() {
    RecordingView(outputFile = File(""),
        onRecord = { /*TODO*/ }, onStop = { /*TODO*/ }, onSave = { /*TODO*/ }) {

    }
}