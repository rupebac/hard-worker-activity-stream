package auth.controller;

import auth.api.BitbucketOAuth2Client;
import com.google.gson.Gson;
import spark.QueryParamsMap;
import spark.utils.StringUtils;
import java.util.Map;
import static spark.Spark.get;

public class BitbucketOAuth2Controller {

	public BitbucketOAuth2Controller() {
		get("/api/v0/bitbucket/auth", (req, res) -> {
			res.redirect(new BitbucketOAuth2Client().buildRedirectUrl(), 302);
			return "";
		});

		get("/api/v0/bitbucket/callback", (req, res) -> {
			final QueryParamsMap queryMap = req.queryMap();
			final String authCode = queryMap.get("code").hasValue() ? queryMap.get("code").value() : null;
			String accessToken;

			if (StringUtils.isNotBlank(authCode)){
				accessToken = new BitbucketOAuth2Client().getAccessToken(authCode);
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

			res.redirect("/home?bitbucketAccessToken=" + accessToken, 302);
			return  "";
		});
	}
}