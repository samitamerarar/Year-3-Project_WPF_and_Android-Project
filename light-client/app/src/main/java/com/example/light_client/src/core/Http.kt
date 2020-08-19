package com.example.light_client.src.core

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class Http {
    data class HttpResponse<T>(var code: Int, var data: T)

    companion object {

        inline fun <reified T> get(url: String, crossinline handler: (res: HttpResponse<T>) -> Unit) {
            Async {
                val conn = URL(url)
                with(conn.openConnection() as HttpURLConnection) {
                    val stream = BufferedInputStream(inputStream)
                    val data = readStream(stream)
                    handler(HttpResponse(responseCode, Json.parse(data)))
                }
            }
        }

        inline fun <reified T> post(url: String, data: Any?, crossinline handler: (res: HttpResponse<T>) -> Unit) {
            Async {
                val body = if (data != null) Json.toString(data) else ""
                val res = sendPost(url, body)
                handler(HttpResponse(res.code, Json.parse(res.data)))
            }
        }

        fun sendPost(target: String, body: String): HttpResponse<String> {
            val url = URL(target)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                instanceFollowRedirects = false
                doOutput = true
                doInput = true
                useCaches = false
                setRequestProperty("Content-Type", "application/json; charset=utf-8")

                try {
                    connect()
                    val os = outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(body)
                    writer.flush()
                    writer.close()
                    os.close()

                    val stream = BufferedInputStream(inputStream)
                    return HttpResponse(responseCode, readStream(stream))
                } catch (e: Exception) {
                    val stream = BufferedInputStream(errorStream)
                    return HttpResponse(responseCode, readStream(stream))
                } finally {
                    disconnect()
                }
            }
        }

        fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
    }
}
