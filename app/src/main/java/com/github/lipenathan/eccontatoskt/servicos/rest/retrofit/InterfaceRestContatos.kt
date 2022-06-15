package com.github.lipenathan.eccontatoskt.servicos.rest.retrofit

import com.github.lipenathan.eccontatoskt.servicos.rest.to.LoginRestTO
import com.github.lipenathan.eccontatoskt.servicos.rest.to.TokenTO
import com.github.lipenathan.eccontatoskt.dominio.Contato
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface InterfaceRestContatos {
    @GET("contatos/lista")
    fun buscarTodos(@Header("Authorization") token: TokenTO): Call<MutableList<Contato>>

    @PUT("contatos/novo")
    fun cadastrar(@Body contato: Contato, @Header("Authorization") token: TokenTO): Call<Contato>

    @PUT("contatos/deletar/{id}")
    fun deletar(@Path("id") id: Int?, @Header("Authorization") token: TokenTO): Call<Contato>

    @POST("autenticacao")
    fun getToken(@Body login: LoginRestTO): Observable<TokenTO>
}