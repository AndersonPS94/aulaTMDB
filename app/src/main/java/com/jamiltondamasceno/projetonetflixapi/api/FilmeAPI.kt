package com.jamiltondamasceno.projetonetflixapi.api

import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeAPI {
    @GET("movie/latest")
    suspend fun recuperarFilmeRecente(): Response<FilmeRecente>

    @GET("movie/popular")
    suspend fun recuperarFilmesPopulares(
        @Query("page") pagina: Int
    ): Response<FilmeResposta>

}