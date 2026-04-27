package com.mohamedfaridelsherbini.screenblock.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusTimer @Inject constructor() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var timerJob: Job? = null

    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    fun start(seconds: Long, onTick: (Long) -> Unit, onFinish: () -> Unit) {
        timerJob?.cancel()
        _remainingSeconds.value = seconds
        timerJob = scope.launch {
            while (_remainingSeconds.value > 0) {
                onTick(_remainingSeconds.value)
                delay(1000)
                _remainingSeconds.value -= 1
            }
            onTick(0)
            onFinish()
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        _remainingSeconds.value = 0
    }
}
