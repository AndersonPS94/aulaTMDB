package com.jamiltondamasceno.projetonetflixapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityDetalhesBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )


        val bundle = intent.extras
        if (bundle != null) {
            val filme = bundle.getParcelable<Filme>("filme") as Filme

            if (filme != null) {
                binding.textFilmeTitulo.text = filme.title
                val nomeFilme = filme.backdrop_path
                val tamanhoFilme = "w780"
                val urlBase = RetrofitService.BASE_URL_IMAGE
                val urlFilme = urlBase + tamanhoFilme + nomeFilme
                Picasso.get()
                    .load(urlFilme)
                    .into(binding.imgPoster)

            }
        }
    }
}