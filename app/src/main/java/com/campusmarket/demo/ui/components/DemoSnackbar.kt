package com.campusmarket.demo.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Every "real" action in the app (publish, send, pay, …) ends up here and only shows a
 * snackbar. One instance lives at the root so messages survive navigation.
 */
@Stable
class DemoActions(
    private val scope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
) {
    fun show(message: String = DEFAULT_MESSAGE) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                duration = SnackbarDuration.Short,
            )
        }
    }

    companion object {
        const val DEFAULT_MESSAGE = "This is a demo – nothing happens here"
    }
}

@Composable
fun rememberDemoActions(): DemoActions {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    return remember(scope, hostState) { DemoActions(scope, hostState) }
}
