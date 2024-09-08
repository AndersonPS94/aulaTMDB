package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val construtorRequisicao = chain.request().newBuilder()


        /*val urlAtual = chain.request().url()
        val novaUrl = urlAtual.newBuilder()
        novaUrl.addQueryParameter("api_key", RetrofitService.API_KEY)
        construtorRequisicao.url(novaUrl.build())*/

        val requisicao = construtorRequisicao.addHeader(
            "Authorization", "Bearer ${RetrofitService.TOKEN}"
        ).build()

        return chain.proceed(requisicao)
    }
}