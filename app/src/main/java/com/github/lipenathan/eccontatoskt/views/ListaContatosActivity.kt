package com.github.lipenathan.eccontatoskt.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lipenathan.eccontatoskt.R
import com.github.lipenathan.eccontatoskt.views.recyclerviews.ContatosRVAdapter
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.retrofit.InterfaceRestContatos
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.retrofit.RetrofitInstance
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.to.TokenTO
import com.github.lipenathan.eccontatosrefactor.modelo.dominio.Contato
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ListaContatosActivity : AppCompatActivity() {

    private var contatos: MutableList<Contato>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var restContatos: InterfaceRestContatos
    private val loginRest = RetrofitInstance.LOGIN_REST
    private val token = TokenTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatos)
        vincularViews()
        buscarContatos()
    }

    private fun vincularViews() {
        recyclerView = findViewById(R.id.rv_contatos)
        findViewById<FloatingActionButton>(R.id.fab_novo_contato).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(this@ListaContatosActivity, CadastroContatosActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun setarRecyclerView() {
        val adapter = ContatosRVAdapter(this, contatos)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@ListaContatosActivity)
    }

    private fun buscarContatos() {
        restContatos = RetrofitInstance.getRestContatos()
        val tokenObervable = restContatos.getToken(loginRest)
        tokenObervable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<TokenTO> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: TokenTO) {
                    token.token = "Bearer ${t.token}"
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@ListaContatosActivity, e.message, LENGTH_LONG).show()
                }

                override fun onComplete() {
                    chamadaRestListarContatos()
                }
            })
    }

    private fun chamadaRestListarContatos() {
        restContatos.buscarTodos(token).enqueue(object : Callback<MutableList<Contato>> {
            override fun onResponse(call: Call<MutableList<Contato>>, response: Response<MutableList<Contato>>) {
                if (response.isSuccessful) {
                    contatos = response.body()
                    setarRecyclerView()
                    Toast.makeText(this@ListaContatosActivity, response.errorBody()?.string(), LENGTH_LONG).show()
                } else {
                    try {
                        Toast.makeText(this@ListaContatosActivity, response.errorBody()?.string(), LENGTH_LONG).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Contato>>, t: Throwable) {
                Toast.makeText(this@ListaContatosActivity, t.message, LENGTH_LONG).show()
            }
        })
    }
}