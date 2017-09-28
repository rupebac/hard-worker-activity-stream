package auth

import auth.controller.BitbucketOAuth2Controller
import auth.controller.CalendarOAuth2Controller
import spark.Spark.get
import spark.utils.StringUtils

class OAuth2Controller {
    init {
        BitbucketOAuth2Controller()
        CalendarOAuth2Controller()


        get("/home") { req, res ->
            val calendarAccessToken = req.queryMap().get("calendarAccessToken").value()
            val bitbucketAccessToken = req.queryMap().get("bitbucketAccessToken").value()

            if (StringUtils.isNotBlank(calendarAccessToken)) {
                res.cookie("calendarAccessToken", calendarAccessToken, 3600)
            }

            if (StringUtils.isNotBlank(bitbucketAccessToken)) {
                res.cookie("bitbucketAccessToken", bitbucketAccessToken, 3600)
            }

            "<html><script>window.location.href='/#auth'</script></html>"
        }
    }
}
