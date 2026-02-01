package com.danylom73.rescuehelper.mvi.nearby

sealed interface NearbySideEffect {
    data class ShowToast(val message: String) : NearbySideEffect
    data class SetFlashlight(val enabled: Boolean) : NearbySideEffect
}
