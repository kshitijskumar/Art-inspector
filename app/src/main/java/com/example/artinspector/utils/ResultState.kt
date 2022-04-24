package com.example.artinspector.utils

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val errorMsg: String) : ResultState<Nothing>()
}
