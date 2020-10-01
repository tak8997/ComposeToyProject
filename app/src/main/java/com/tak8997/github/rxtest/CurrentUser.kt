package com.tak8997.github.rxtest

class CurrentUser: CurrentUserType {

    private val users = mutableMapOf<String, UserEnvelope>()

    override fun login(userEnvelope: UserEnvelope) {
        users[userEnvelope.name] = userEnvelope
    }
}

interface CurrentUserType {

    fun login(name: UserEnvelope)
}