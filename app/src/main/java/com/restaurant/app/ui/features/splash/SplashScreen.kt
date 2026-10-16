package com.restaurant.app.ui.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.ui.features.splash.components.SplashBody
import com.restaurant.app.ui.features.splash.components.SplashFooter
import com.restaurant.app.ui.theme.AppTheme
import com.restaurant.app.ui.theme.SplashGradientBottom

/** Pantalla conectada al ViewModel: recibe el estado y avisa cuando termina. */
@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.finished.collect { onFinished() }
    }

    SplashContent(state)
}

/** Solo dibuja la UI a partir del estado. Sin ViewModel, fácil de previsualizar. */
@Composable
fun SplashContent(
    state: SplashUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        SplashGradientBottom
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        SplashBody(modifier = Modifier.align(Alignment.Center))
        SplashFooter(
            appVersion = state.appVersion,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        SplashContent(SplashUiState(appVersion = "1.0"))
    }
}