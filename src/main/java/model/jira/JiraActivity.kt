package model.jira

import model.Act
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.junit.platform.commons.util.StringUtils
import java.time.ZonedDateTime

class JiraActivity(entry: JSONObject) : Act() {
    init {
        type = "jira"
        id = entry.get("id").toString()

        link = entry.getJSONArray("link").getJSONObject(0).getString("href")
        if((link as String).contains("?")){
            link = (link as String).substring(0, link!!.indexOf("?"))
        }


        try {
            summary = entry.getJSONObject("activity:object")?.getJSONObject("summary")?.getString("content")
        } catch (e: JSONException) {
            //Gotcha!
        }
        val html = (entry.getJSONObject("title")).getString("content")
        if(StringUtils.isBlank(summary)){
            summary = Jsoup.parse(html).text()
        }
        description = Jsoup.parse(html).text()
        val zdt = ZonedDateTime.parse(entry.getString("published"))
        date = zdt.minusHours(3)
        userName = entry.getJSONObject("author").getString("name")
    }
}
