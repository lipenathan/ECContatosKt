package com.github.lipenathan.eccontatoskt.servicos.rest.retrofit

import com.github.lipenathan.eccontatoskt.servicos.rest.to.LoginRestTO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private val URL = "http://10.249.6.26:8080/estudo-caso-jwt/api/v1/"
        val LOGIN_REST = LoginRestTO("uniprime", "abc123")

        fun getRestContatos(): InterfaceRestContatos {
            return Retrofit.Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(InterfaceRestContatos::class.java)
        }

        private fun getHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
        }
    }
}