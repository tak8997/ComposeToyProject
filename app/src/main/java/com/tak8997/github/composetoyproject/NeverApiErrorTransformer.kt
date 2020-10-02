package com.tak8997.github.composetoyproject


class ErrorEnvelope {

    companion object {

        fun fromThrowable(t: Throwable?): ErrorEnvelope? {
            if (t is ApiException) {
                val exception: ApiException = t as ApiException
                return exception.errorEnvelope()
            }
            return null
        }
    }

    fun errorMessage() = "this is error"
}

class ApiException(private val errorEnvelope: ErrorEnvelope) : ResponseException() {

    fun errorEnvelope(): ErrorEnvelope {
        return errorEnvelope
    }
}

open class ResponseException() : RuntimeException() {


}
