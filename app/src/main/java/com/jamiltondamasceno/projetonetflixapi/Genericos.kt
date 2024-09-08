package com.jamiltondamasceno.projetonetflixapi

fun minhaFuncao( vararg itens: String){
    itens.forEach { item ->
        println("Item: $item")
    }
}

fun main() {
    minhaFuncao("Ana", "Julia", "Victoria")
}