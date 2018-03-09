package model.jira

import model.Act
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.platform.commons.util.StringUtils
import java.time.ZonedDateTime

class JiraActivity(entry: JSONObject) : Act() {
    init {
        type = "jira"
        id = entry.get("id").toString()
        link = entry.getJSONArray("link").getJSONObject(0).getString("href")
        //try { summary = entry.getJSONObject("activity:object")?.getJSONObject("summary")?.getString("content") } catch (e: JSONException) {}
        if(StringUtils.isNotBlank(summary)){
            val html = (entry.getJSONObject("title")).getString("content")
            summary = Jsoup.parse(html).text()
        }
        val zdt = ZonedDateTime.parse(entry.getString("published"))
        date = zdt.minusHours(3)
        userName = entry.getJSONObject("author").getString("name")
    }
}
