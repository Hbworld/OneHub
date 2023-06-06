package com.hbworld.onehub.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hbworld.onehub.dtos.Results
import com.hbworld.onehub.ui.screens.ErrorScreen
import com.hbworld.onehub.ui.screens.ListDevicesScreen
import com.hbworld.onehub.ui.screens.LoadingScreen
import com.hbworld.onehub.ui.screens.SplashScreen
import com.hbworld.onehub.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }

    }

    @Composable
    private fun MyApp(viewModel: MyViewModel = viewModel()) {

        var showSplash by rememberSaveable { mutableStateOf(true) }
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
            if (showSplash) SplashScreen(onButtonClick = {
                viewModel.searchForAvailableServices()
                showSplash = !showSplash
            })
            else {
                val results by viewModel.devices.collectAsState()
                when (results) {
                    is Results.loading -> {
                        LocalContext.current.showToast("Device Discovery Started")
                        LoadingScreen()
                    }

                    is Results.success -> {
                        ListDevicesScreen(
                            list = (results as Results.success).data,
                            refreshBtnClick = { viewModel.searchForAvailableServices() })
                    }

                    is Results.error -> {
                        ErrorScreen(
                            message = (results as Results.error).message,
                            onButtonClick = { viewModel.searchForAvailableServices() })
                    }
                }
            }

        }
    }


}