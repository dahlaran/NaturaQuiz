package com.dahlaran.naturaquiz.core.bus

import com.dahlaran.naturaquiz.core.data.RepoError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * EventBus is a singleton that allows to send events to the UI layer.
 * It uses a Channel to send events and a Flow to receive them.
 * The events are of type [Any] to not be forced to create a when will all the possible events.
 */
object EventBus {
    private val _events = Channel<Any>()
    val events = _events.receiveAsFlow()

    /**
     * Send an event to the UI layer
     *
     * @param event The event to send
     */
    suspend fun sendEvent(event: Event) {
        _events.send(event)
    }
}

sealed interface Event {
    data class ToastError(val error: RepoError) : Event
    data object NavigateToHomeScreen : Event
}