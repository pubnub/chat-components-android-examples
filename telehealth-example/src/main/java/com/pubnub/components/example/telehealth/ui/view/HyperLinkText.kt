package com.pubnub.components.example.telehealth.ui.view

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.pubnub.components.example.telehealth.ui.theme.HyperLinkColor

@Composable
fun HyperlinkText(
    fullText: String,
    hyperlink: Hyperlink,
    modifier: Modifier = Modifier,
    linkTextColor: Color = HyperLinkColor,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        val startIndex = fullText.indexOf(hyperlink.text)
        val endIndex = startIndex + hyperlink.text.length
        addStyle(
            style = SpanStyle(
                color = linkTextColor,
                fontSize = fontSize,
                fontWeight = linkTextFontWeight,
                textDecoration = linkTextDecoration
            ),
            start = startIndex,
            end = endIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = hyperlink.link,
            start = startIndex,
            end = endIndex
        )
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

data class Hyperlink(val text: String, val link: String)