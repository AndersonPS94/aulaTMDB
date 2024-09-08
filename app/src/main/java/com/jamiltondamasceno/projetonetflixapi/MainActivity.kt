package com.jamiltondamasceno.projetonetflixapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.jamiltondamasceno.projetonetflixapi.model.Genero
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var paginaAtual = 1
    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }
    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null
    var gridLayoutManager : GridLayoutManager? = null
    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarViews()

        /*val genero1 = Genero(1, "Comedia")
        val genero2 = Genero(2, "Ação")


        val retro = RetrofitSingleton
        Log.i("api_filme", "retrofit: $retro ")
        Log.i("api_filme","genero1: $genero1 - genero2: $genero2")
*/
    }
    private fun inicializarViews() {
        filmeAdapter = FilmeAdapter{filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter
        gridLayoutManager = GridLayoutManager(
            this,
            2
        )
        /*gridLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)*/

        binding.rvPopulares.layoutManager = gridLayoutManager

        binding.rvPopulares.addOnScrollListener(object : OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val podeDescerVerticalmente = recyclerView.canScrollVertically(1)
                if (!podeDescerVerticalmente){
                    recuperarFilmesPopularesProximaPagina()
                }
                /*val ultimoItemVisivel = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                val totalItens = linearLayoutManager?.itemCount

                if (ultimoItemVisivel!== null && totalItens != null){
                    if (totalItens - 1 == ultimoItemVisivel){
                        binding.fabAdicionar.hide()
                }else {
                        binding.fabAdicionar.show()
                    }
                }*/
                /*Log.i("recycler_test", "onScrolled: dx: $dx  dy: $dy")

                if (dy > 0){
                    binding.fabAdicionar.hide()
                }else{
                    binding.fabAdicionar.show()
                }*/
            }
        })

        /*class ScrollCustomizado: OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }*/
    }

    override fun onStart() {
            super.onStart()
            recuperarFilmeRecente()
            recuperarFilmesPopulares()

        }

        private fun recuperarFilmesPopularesProximaPagina() {
            if (paginaAtual < 1000) {
                paginaAtual++
                recuperarFilmesPopulares(paginaAtual)
            }
        }


        private fun recuperarFilmesPopulares(pagina: Int = 1) {
            jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {
                var resposta: Response<FilmeResposta>? = null

                try {
                    resposta = filmeAPI.recuperarFilmesPopulares(pagina)
                }catch (e: Exception){
                    exibirMensagem("erro ao fazer a requisição")
                }
                if (resposta != null){
                    if (resposta.isSuccessful){
                        val filmeResposta = resposta.body()
                        val listaFilmes = filmeResposta?.filmes
                        if (listaFilmes != null && listaFilmes.isNotEmpty()){

                            withContext(Dispatchers.Main){
                                filmeAdapter.adicionarLista(listaFilmes)
                            }

                            /*Log.i("filmes_api", "lista filmes: ")
                            listaFilmes.forEach { filme ->
                                Log.i("filmes_api", "titulo: ${filme.title}")
                            }*/
                        }

                    }else{
                        exibirMensagem("não foi possivel recuperar os filmes populares")
                    }
                }
            }
        }

        private fun recuperarFilmeRecente() {
            jobFilmeRecente = CoroutineScope(Dispatchers.IO).launch {
                    var resposta :Response <FilmeRecente>?= null

                try {
                    resposta = filmeAPI.recuperarFilmeRecente()
                }catch (e: Exception){
                    exibirMensagem("erro ao fazer a requisição")
                }

                if (resposta != null){
                        if (resposta.isSuccessful){
                            val filmeRecente = resposta.body()
                            val nomeImagem = filmeRecente?.poster_path
                            val url = RetrofitService.BASE_URL_IMAGE + "w780" + nomeImagem

                            withContext(Dispatchers.Main){
                                Picasso.get()
                                    .load(url)
                                    .error(R.drawable.capa)
                                    .into(binding.imgCapa)
                            }
                        }else {
                            exibirMensagem("não foi possivel recuperar o filme recente")
                        }
                }else {
                    exibirMensagem("nao foi possivel fazer a requisição")
                }
            }
        }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel()
        jobFilmesPopulares?.cancel()
    }
}