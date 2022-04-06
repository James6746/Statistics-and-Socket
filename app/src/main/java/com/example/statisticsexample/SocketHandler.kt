package com.example.statisticsexample;


interface SocketHandler {
    fun onResponse(response: CurrencyResponse)
    fun onFailure(t: Throwable)
}