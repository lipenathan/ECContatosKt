package com.github.lipenathan.eccontatoskt.servicos.rest.to

data class TokenTO (var token: String = "") {
    override fun toString(): String {
        return token
    }
}