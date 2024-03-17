package edu.temple.convoy.main_convoy.audio.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.convoy.R
import edu.temple.convoy.ui.components.CustomRoundButton
import edu.temple.convoy.ui.components.CustomText
import java.io.File

@Composable
fun RecordingView(
    onRecord: () -> Unit,
    onStop: () -> Unit,
    onCancel: () -> Unit,
    onSend: () -> Unit,
) {
    var isRecording by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isRecording) {
            Text(
                text = "Now recording...",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Text(
                text = "Press play to start recording",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly)
        {
            CustomRoundButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.baseline_cancel_24
            ) {
                onCancel()
            }

            PlayerButton(
                modifier = Modifier.weight(1f),
                onPlay = {
                    isRecording = true
                    onRecord() },
                onPause = {
                    isRecording = false
                    onStop() }
            )

            CustomRoundButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.baseline_send_24
            ) {
                onSend()
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview
@Composable
fun RecordingDialogPreview() {
    RecordingView(
        onRecord = { /*TODO*/ }, onStop = { /*TODO*/ }, onCancel = { /*TODO*/ }) {
    }
}