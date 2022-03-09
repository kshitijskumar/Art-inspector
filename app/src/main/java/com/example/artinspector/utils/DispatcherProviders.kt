package com.example.artinspector.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProviders {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}