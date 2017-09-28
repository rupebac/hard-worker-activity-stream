package service.jira

import api.jira.JiraFeedClient
import model.IAct
import model.jira.JiraActivity
import org.json.JSONObject
import java.time.ZonedDateTime

internal class JiraService {

    fun getActivities(user: String, date: ZonedDateTime): List<IAct> {

        val entries = JiraFeedClient().getActivityStream(user, date)

        return entries.map { JiraActivity(it as JSONObject) }
    }

}
