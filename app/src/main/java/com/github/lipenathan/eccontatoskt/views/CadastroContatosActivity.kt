package com.github.lipenathan.eccontatoskt.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.github.lipenathan.eccontatoskt.R
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.retrofit.InterfaceRestContatos
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.retrofit.RetrofitInstance
import com.github.lipenathan.eccontatosrefactor.modelo.dataaccess.to.TokenTO
import com.github.lipenathan.eccontatosrefactor.modelo.dominio.Contato
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CadastroContatosActivity : AppCompatActivity() {

    private lateinit var txNome: EditText
    private lateinit var txEmail: EditText
    private lateinit var btCadastrar: Button
    private lateinit var restContatos: InterfaceRestContatos
    private var token = TokenTO()
    private val loginRest = RetrofitInstance.LOGIN_REST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_contatos)
        restContatos = RetrofitInstance.getRestContatos()
        vincularViews()
        btCadastrar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var novoContato = Contato()
                novoContato.nome = txNome.text.toString()
                novoContato.email = txEmail.text.toString()
                cadastrarContato(novoContato)
            }
        })
    }

    private fun vincularViews() {
        txNome = findViewById(R.id.tx_nome_novo_contato)
        txEmail = findViewById(R.id.tx_email_novo_contato)
        btCadastrar = findViewById(R.id.bt_cadastrar)
    }

    private fun cadastrarContato(novoContato: Contato) {
        val tokenObersable = restContatos.getToken(loginRest)
        tokenObersable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<TokenTO> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: TokenTO) {
                    token.token = "Bearer ${t.token}"
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@CadastroContatosActivity, e.message, Toast.LENGTH_LONG).show()
                }

                override fun onComplete() {
                    chamadaRestCadastrarContato(novoContato)
                }
            })
    }

    private fun chamadaRestCadastrarContato(novoContato: Contato) {
        restContatos.cadastrar(novoContato, token).enqueue(object : Callback<Contato> {
            override fun onResponse(call: Call<Contato>, response: Response<Contato>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastroContatosActivity, "Contato salvo com sucesso", LENGTH_LONG).show()
                    val intent = Intent(this@CadastroContatosActivity, ListaContatosActivity::class.java)
                    startActivity(intent)
                } else {
                    try {
                        Toast.makeText(this@CadastroContatosActivity, response.errorBody()?.string(), LENGTH_LONG).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Contato>, t: Throwable) {
                Toast.makeText(this@CadastroContatosActivity, t.message, LENGTH_LONG).show()
            }
        })
    }

}