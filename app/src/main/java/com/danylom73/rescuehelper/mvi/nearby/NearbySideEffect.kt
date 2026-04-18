package com.danylom73.rescuehelper.mvi.nearby

import com.danylom73.rescuehelper.presentation.components.base.BaseSnackbarIcon

sealed interface NearbySideEffect {
    data class ShowMessage(
        val message: String,
        val icon: BaseSnackbarIcon? = null
    ) : NearbySideEffect
}
