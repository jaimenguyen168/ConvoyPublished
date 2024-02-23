package edu.temple.convoy.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import edu.temple.convoy.ui.theme.Purple40

@Composable
fun CustomText(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    color: Color = Color.Black,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            color = color,
            textAlign = textAlign
        )
    )
}

@Composable
fun CustomClickableText(
    text: String,
    goToText: String,
    onTextClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append(text)
        append(" ")
        withStyle(style = SpanStyle(color = Purple40)) {
            pushStringAnnotation(tag = goToText, annotation = goToText)
            append(goToText)
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { onTextClick() }
    )
}