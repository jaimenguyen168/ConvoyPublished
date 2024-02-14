package edu.temple.convoy.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import edu.temple.convoy.ui.theme.Purple40

@Composable
fun CustomizedDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = 5.dp,
            shape = RoundedCornerShape(15.dp),
            modifier = modifier
                .fillMaxWidth(0.8f)
                .border(
                    border = BorderStroke(1.dp, Purple40),
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )

                Divider(Modifier.padding(16.dp))

                CustomizedText(
                    text = content,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Divider(Modifier.padding(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CustomizedButton(
                        text = "Cancel",
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    CustomizedButton(
                        text = "Confirm",
                        onClick = { onConfirm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog() {
    CustomizedDialog("", "", onDismiss = { /*TODO*/ }, onConfirm = { /*TODO*/ })
}