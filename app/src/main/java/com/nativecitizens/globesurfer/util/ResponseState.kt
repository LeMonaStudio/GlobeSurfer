package com.nativecitizens.globesurfer.util



sealed class ResponseState<out T> {
    data class Success<T>(val response: T?) : ResponseState<T>()
    data class Error(val responseErrorMessage: String) : ResponseState<Nothing>()
    object Loading : ResponseState<Nothing>()
    object NotCalled: ResponseState<Nothing>()
}