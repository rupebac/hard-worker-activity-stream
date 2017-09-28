package auth.controller;

import auth.api.CalendarOAuth2Client;
import com.google.gson.Gson;
import model.Response;
import spark.QueryParamsMap;
import spark.utils.StringUtils;
import java.util.Map;
import static spark.Spark.get;

public class CalendarOAuth2Controller {

	public CalendarOAuth2Controller() {
		final Gson gson = new Gson();

		get("/api/v0/calendar/auth", (req, res) ->
				new Response(200, new CalendarOAuth2Client().buildRedirectUrl()), gson::toJson);


		get("/api/v0/calendar/callback", (req, res) -> {
			final QueryParamsMap queryMap = req.queryMap();
			final String authCode = queryMap.get("code").hasValue() ? queryMap.get("code").value() : null;
			String accessToken;

			if (StringUtils.isNotBlank(authCode)){
				accessToken = new CalendarOAuth2Client().getAccessToken(authCode);
			} else {
				res.redirect("/404", 302);
				return  "";
			}

			Map mapToken = new Gson().fromJson(accessToken, Map.class);
			accessToken = (String) mapToken.get("access_token");

			if(StringUtils.isBlank(accessToken)){
				res.redirect("/404", 302);
				return  "";
			}

			res.redirect("/home?calendarAccessToken=" + accessToken, 302);
			return  "";
		});
	}
}