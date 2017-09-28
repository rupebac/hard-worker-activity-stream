package api

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException

abstract class ClientApi {

    fun performRequest(type: String, accessToken: String, url: String): String {
        val httpClient = HttpClientBuilder.create().build()
        val request = HttpGet(url)
        request.addHeader("Authorization", "$type $accessToken")

        return try {
            val httpResponse: HttpResponse
            httpResponse = httpClient.execute(request)
            EntityUtils.toString(httpResponse.getEntity(), "UTF-8")
        } catch (e: IOException){
            ""
        }
    }
}
