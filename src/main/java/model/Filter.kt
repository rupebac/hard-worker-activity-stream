package model

import spark.QueryParamsMap
import java.time.ZonedDateTime

class Filter(queryMap: QueryParamsMap) {
    val date: ZonedDateTime = ZonedDateTime.parse(queryMap.get("date").value())
    val user: String? = queryMap.get("jiraUser")?.value()
    var repoList: List<String>? = null

    init{
        val repos: String? = queryMap.get("repoList")?.value()
        if(!repos.isNullOrBlank()){
            repoList = repos
                    ?.trim()
                    ?.replace("[","")
                    ?.replace("]","")
                    ?.split(',')
        }
    }

}
