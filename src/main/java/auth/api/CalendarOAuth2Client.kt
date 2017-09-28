package auth.api

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import java.io.FileInputStream
import java.io.IOException
import java.util.*


class CalendarOAuth2Client {

    private val client_id:String
    private val client_secret:String

    init{
        val props = Properties()
        val file = FileInputStream("src/main/resources/config.properties")
        props.load(file)
        client_id = props.getProperty("prop.calendar.client_id")
        client_secret = props.getProperty("prop.calendar.client_secret")
    }

    fun buildRedirectUrl(): String {
        return "$URL?client_id=$client_id&redirect_uri=$REDIRECT_URI" +
                "&response_type=code&scope=https://www.googleapis.com/auth/calendar.readonly&state=xyz"
    }

    @Throws(IOException::class)
    fun getAccessToken(authorizationCode: String): String {

        val post = HttpPost("https://accounts.google.com/o/oauth2/token")

        val params = ArrayList<NameValuePair>()
        params.add(BasicNameValuePair("code", authorizationCode))
        params.add(BasicNameValuePair("client_id", client_id))
        params.add(BasicNameValuePair("client_secret", client_secret))
        params.add(BasicNameValuePair("redirect_uri", REDIRECT_URI))
        params.add(BasicNameValuePair("grant_type", "authorization_code"))

        post.entity = UrlEncodedFormEntity(params, HTTP.UTF_8)

        val httpClient = HttpClientBuilder.create().build()
        val response = httpClient.execute(post)

        val token = EntityUtils.toString(response.entity, "UTF-8")

        return token
    }

    companion object {

        private val URL = "https://accounts.google.com/o/oauth2/v2/auth"
        private val REDIRECT_URI = "http://localhost:8080/api/v0/calendar/callback"
    }

}
