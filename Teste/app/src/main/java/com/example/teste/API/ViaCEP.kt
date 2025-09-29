package com.example.teste.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class CepResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?,
    val erro: Boolean? = null // viaCEP retorna "erro": true se inválido
)

interface ViaCepService {
    @GET("{cep}/json/")
    fun getEndereco(@Path("cep") cep: String): Call<CepResponse>
}
