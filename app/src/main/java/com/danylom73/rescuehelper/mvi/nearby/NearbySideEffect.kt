package com.danylom73.rescuehelper.mvi.nearby

sealed interface NearbySideEffect {
    data class ShowToast(val message: String) : NearbySideEffect
}
