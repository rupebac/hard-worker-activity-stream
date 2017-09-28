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

class BitbucketOAuth2Client {

    private val client_id:String
    private val client_secret:String

    init{
        val props = Properties()
        val file = FileInputStream("src/main/resources/config.properties")
        props.load(file)
        client_id = props.getProperty("prop.bitbucket.client_id")
        client_secret = props.getProperty("prop.bitbucket.client_secret")
    }

    fun buildRedirectUrl(): String {
        return "$BITBUCKET_OAUTH2_AUTHORIZE?client_id=$client_id&response_type=code&state=xyz"
    }

    @Throws(IOException::class)
    fun getAccessToken(authorizationCode: String): String {

        val params = defaultParams
        params.add(BasicNameValuePair("code", authorizationCode))
        params.add(BasicNameValuePair("grant_type", "authorization_code"))

        val token = performPost(params)

        return token
    }

    @Throws(IOException::class)
    fun refreshToken(refreshToken: String): String {

        val params = defaultParams
        params.add(BasicNameValuePair("refresh_token", refreshToken))
        params.add(BasicNameValuePair("grant_type", "refresh_token"))

        val token = performPost(params)

        return token
    }

    private val defaultParams: MutableList<NameValuePair>
        get() {
            val params = ArrayList<NameValuePair>()
            params.add(BasicNameValuePair("client_id", client_id))
            params.add(BasicNameValuePair("client_secret", client_secret))
            return params
        }

    @Throws(IOException::class)
    private fun performPost(params: List<NameValuePair>): String {
        val post = HttpPost(BITBUCKET_OAUTH2_ACCESS_TOKEN)
        post.entity = UrlEncodedFormEntity(params, HTTP.UTF_8)

        val httpClient = HttpClientBuilder.create().build()
        val response = httpClient.execute(post)

        return EntityUtils.toString(response.entity, "UTF-8")
    }

    companion object {

        private val BITBUCKET_OAUTH2_AUTHORIZE = "https://bitbucket.org/site/oauth2/authorize"
        private val BITBUCKET_OAUTH2_ACCESS_TOKEN = "https://bitbucket.org/site/oauth2/access_token"
    }


}
