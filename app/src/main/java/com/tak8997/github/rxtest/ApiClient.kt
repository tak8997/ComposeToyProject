package com.tak8997.github.rxtest

import io.reactivex.Observable


class MockApiClient : ApiClient {

    override fun signup(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Observable<UserEnvelope> {

        return if (password == passwordConfirmation) {
            Observable.just(UserEnvelope(name))
        } else {
            Observable.create { it.onError(ApiException(ErrorEnvelope())) }
        }
    }
}

interface ApiClient {

    fun signup(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Observable<UserEnvelope>
}

data class UserEnvelope(
    val name: String
)