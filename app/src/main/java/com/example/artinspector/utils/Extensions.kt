package com.example.artinspector.utils

import retrofit2.Response
import java.lang.Exception

suspend fun <T>safeApiCall(errorMsg: String? = null, api: suspend () -> Response<T>): ResultState<T> {
    return try {
        val response = api.invoke()
        if (response.isSuccessful) {
            if (response.body() != null) {
                ResultState.Success(response.body()!!)
            } else {
                ResultState.Error("Api call successful but server not working")
            }
        } else {
            ResultState.Error("Some api error occurred")
        }
    } catch (e: Exception) {
        ResultState.Error(errorMsg ?: "Something went wrong")
    }
}

inline fun <reified T>Any?.performIfInstanceOf(action: T.() -> Unit) {
    if (this is T) {
        this.action()
    }
}