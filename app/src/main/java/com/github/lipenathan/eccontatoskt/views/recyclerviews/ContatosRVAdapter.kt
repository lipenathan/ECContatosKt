package com.github.lipenathan.eccontatoskt.views.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.RecyclerView
import com.github.lipenathan.eccontatoskt.R
import com.github.lipenathan.eccontatoskt.servicos.rest.retrofit.InterfaceRestContatos
import com.github.lipenathan.eccontatoskt.servicos.rest.retrofit.RetrofitInstance
import com.github.lipenathan.eccontatoskt.servicos.rest.to.TokenTO
import com.github.lipenathan.eccontatoskt.dominio.Contato
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContatosRVAdapter(val context: Context, val contatos: MutableList<Contato>?) : RecyclerView.Adapter<ContatosRVAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_view_contatos_item, parent, false)
        return MyViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txNome.setText(contatos?.get(position)?.nome)
        holder.txEmail.setText(contatos?.get(position)?.email)
        holder.idContato = contatos?.get(position)?.id
    }

    override fun getItemCount(): Int {
        return contatos!!.size
    }

    class MyViewHolder(itemView: View, adapter: ContatosRVAdapter) : RecyclerView.ViewHolder(itemView) {
        private val token = TokenTO()
        var idContato: Int? = 0
        var txNome: TextView
        var txEmail: TextView
        var btDeletar: ImageButton
        var adapter: ContatosRVAdapter
        var restContatos: InterfaceRestContatos

        init {
            restContatos = RetrofitInstance.getRestContatos()
            this.adapter = adapter
            this.txNome = itemView.findViewById(R.id.tx_nome_contato)
            this.txEmail = itemView.findViewById(R.id.tx_email_contato)
            this.btDeletar = itemView.findViewById(R.id.bt_deletar)
            setarDeletar()
        }

        companion object {
            private val LoginRestTO = RetrofitInstance.LOGIN_REST
        }

        private fun setarDeletar() {
            btDeletar.setOnClickListener {
                val index = absoluteAdapterPosition
                val tokenObservable = restContatos.getToken(LoginRestTO)
                tokenObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<TokenTO> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: TokenTO) {
                            token.token = "Bearer ${t.token}"
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(adapter.context, e.message, LENGTH_SHORT).show()
                        }

                        override fun onComplete() {
                            chamadaDeletarContato(index)
                        }

                    })
            }
        }

        private fun chamadaDeletarContato(index: Int) {
            val chamada = restContatos.deletar(idContato, token)
            chamada.enqueue(object : Callback<Contato> {
                override fun onResponse(call: Call<Contato>, response: Response<Contato>) {
                    if (response.isSuccessful) {
                        Toast.makeText(adapter.context, "Contado Deletado com sucesso", LENGTH_LONG).show()
                        adapter.contatos?.removeAt(index)
                        adapter.notifyItemRemoved(index)
                    } else {
                        try {
                            Toast.makeText(adapter.context, response.errorBody()?.string(), LENGTH_LONG).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<Contato>, t: Throwable) {
                    Toast.makeText(adapter.context, t.message, LENGTH_LONG).show()
                }
            })
        }
    }
}