package model.jira

import model.Act
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.time.ZonedDateTime

class JiraActivity(entry: JSONObject) : Act() {
    init {
        type = "jira"
        id = entry.get("id").toString()
        link = ((entry.get("link") as JSONArray).get(0) as JSONObject).get("href").toString()
        val html = (entry.get("title") as JSONObject).get("content").toString()
        summary = Jsoup.parse(html).text()
        val zdt = ZonedDateTime.parse(entry.get("published").toString())
        date = zdt.minusHours(3)
        userName = (entry.get("author") as JSONObject).get("name").toString()
    }
}
