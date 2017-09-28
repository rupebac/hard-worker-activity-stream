package api.jira

import api.ClientApi
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import java.io.FileInputStream
import java.io.IOException
import java.time.ZonedDateTime
import java.util.*

class JiraFeedClient : ClientApi() {

    private val baseUrl:String
    private val acessToken:String

    init{
        val props = Properties()
        val file = FileInputStream("src/main/resources/config.properties")
        props.load(file)
        baseUrl = props.getProperty("prop.jira.url")
        acessToken = props.getProperty("prop.jira.access_token")
    }

    fun getActivityStream(user: String, date: ZonedDateTime): JSONArray {
        val url = "${baseUrl}/jira/activity?maxResults=1000&streams=user+IS+$user&streams=update-date+AFTER+${date.toInstant().toEpochMilli()}&streams=update-date+BEFORE+" +
                "${date.plusHours(23).plusMinutes(59).plusSeconds(59).toInstant().toEpochMilli()}" +
                "&os_authType=basic&title=undefined"

        return try {
            val respAsString = performRequest("Basic", acessToken, url)
            (XML.toJSONObject(respAsString).get("feed") as JSONObject).get("entry") as JSONArray
        } catch (e: IOException) {
            JSONArray()
        } catch (e: JSONException) {
            JSONArray()
        }

    }
}
