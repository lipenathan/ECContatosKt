package com.github.lipenathan.eccontatoskt.viewmodel

import com.github.lipenathan.eccontatoskt.dominio.Contato


class CadastroContatoViewModel(): Subject<Contato> {
    override val collection: MutableList<Contato> = mutableListOf()
        get

    override fun attach(o: Contato) {
        TODO("Not yet implemented")
    }

    override fun dettach(o: Contato) {
        TODO("Not yet implemented")
    }

    override fun notifyObservers() {
        TODO("Not yet implemented")
    }
}