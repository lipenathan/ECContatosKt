package com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.to

data class TokenTO (var token: String = "") {
    override fun toString(): String {
        return token
    }
}