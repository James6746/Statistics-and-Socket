package com.example.statisticsexample;
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*

class SocketManager {

    companion object {
        private const val BASE_URL = "wss://ws.bitstamp.net"
        private val client = OkHttpClient()

        private val requestBuilder: Request = Request
            .Builder()
            .url(BASE_URL)
            .build()

        fun connectToSocket(request: Currency, handler: SocketHandler){
            client.newWebSocket(requestBuilder, object : WebSocketListener(){
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    webSocket.send(
                        GsonBuilder()
                        .create()
                        .toJson(request)
                    )
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    handler.onResponse(Gson().fromJson(text, CurrencyResponse::class.java))
                }
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    handler.onFailure(t)
                }
            })
            client.dispatcher.executorService.shutdown()
        }


    }


}