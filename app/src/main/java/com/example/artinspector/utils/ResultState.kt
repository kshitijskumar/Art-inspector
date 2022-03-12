package com.example.artinspector.utils

sealed class ResultState<out T> {
    object Idle : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    object Loading : ResultState<Nothing>()
    data class Error(val errorMsg: String) : ResultState<Nothing>()
}
