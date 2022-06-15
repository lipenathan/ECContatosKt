package com.github.lipenathan.eccontatoskt.viewmodel

interface Subject<T> {
    val collection: MutableList<T>
    fun attach(o: T)
    fun dettach(o: T)
    fun notifyObservers()
}