package com.wholesale.manager.domain.model

data class User(
    val id: String,
    val username: String,
    val passwordHash: String,
    val role: String,
    val displayName: String
) {
    companion object {
        const val ROLE_DIRECTOR = "DIRECTOR"
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_EXECUTOR = "EXECUTOR"
    }
}
