package com.hbworld.onehub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(message: String, onButtonClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(text = message)
        ElevatedButton(
            onClick = onButtonClick,
            modifier = Modifier.padding(top = 28.dp)
        ) {
            Text(text = "Please try again")
        }
    }
}

@Preview
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(message = "An Error occured", {})
}