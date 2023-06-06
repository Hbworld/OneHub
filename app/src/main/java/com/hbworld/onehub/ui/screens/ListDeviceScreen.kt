package com.hbworld.onehub.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hbworld.onehub.dtos.Device
import java.net.InetAddress

@Composable
fun ShowDevicesScreen(list: List<Device>, refreshBtnClick: () -> Unit) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val modifier = Modifier
                .padding(28.dp)
                .width(120.dp)
                .height(50.dp)
                .align(Alignment.End)
            ElevatedButton(
                onClick = refreshBtnClick, modifier = modifier,
            ) {
                Text(text = "Refresh")
            }

            Text(
                text = "Total ${list.size} devices are Online",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp)
            ) {
                items(items = list) { device ->
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 12.dp),
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Name -> ${device.name}",
                                modifier = Modifier
                                    .padding(top = 12.dp, start = 12.dp)
                                    .weight(1f)
                            )
                            Text(
                                text = "Open Ports -> ${device.openPorts}",
                                modifier = Modifier
                                    .padding(top = 12.dp, end = 12.dp)
                                    .weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = device.ipAddress.hostAddress!!,
                                modifier = Modifier
                                    .padding(bottom = 12.dp, start = 12.dp)
                                    .weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Available services -> ${device.serviceTypes}",
                                modifier = Modifier
                                    .padding(start = 12.dp, bottom = 12.dp, end = 12.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun PreviewShowDeviceScreen() {
    ShowDevicesScreen(
        listOf(
            Device(
                ipAddress = InetAddress.getByName("192.168.1.1"),
                name = "Test Device - 1",
                openPorts = mutableSetOf(111, 456, 222),
                serviceTypes = mutableSetOf("aqw, cvf,sde")
            ),
            Device(
                ipAddress = InetAddress.getByName("192.168.1.2"),
                name = "Test Device - 2",
                openPorts = mutableSetOf(333, 412),
                serviceTypes = mutableSetOf("test, demo")
            )
        ), refreshBtnClick = {}
    )
}