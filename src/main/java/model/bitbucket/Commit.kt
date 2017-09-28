package model.bitbucket

import com.google.gson.internal.LinkedTreeMap
import model.Act

import java.time.ZonedDateTime

class Commit(commit: LinkedTreeMap<*, *>) : Act() {

    init {
        type = "commit"
        id = commit["hash"]?.toString()
        link = ((commit["links"] as? LinkedTreeMap<*, *>)?.get("html") as? LinkedTreeMap<*, *>)?.get("href")?.toString()
        summary = (commit["repository"] as? LinkedTreeMap<*, *>)?.get("full_name")?.toString() + " <br> " + commit["message"]?.toString()
        date= ZonedDateTime.parse(commit["date"]?.toString())?.minusHours(3)
        userName = ((commit["author"] as? LinkedTreeMap<*, *>)?.get("user") as? LinkedTreeMap<*, *>)?.get("username")?.toString()
    }

}
